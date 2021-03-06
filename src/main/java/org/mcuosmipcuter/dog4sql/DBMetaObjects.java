/**
*   JDBC database client for application developers and support
*   Copyright (C) 2003 - 2013 Michael Heinzelmann,
*   Michael Heinzelmann IT-Consulting
*
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.mcuosmipcuter.dog4sql;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class DBMetaObjects extends Thread implements TreeModel
{
  protected static final String TYPE_DATABASE = "database";
  protected static final String TYPE_SCHEMA = "schema";
  protected static final String TYPE_TABLE = "table";
  protected static final String TYPE_COLUMN = "column";
  protected static final String TYPE_TYPE = "type";

  private boolean build;
  private boolean buildCompleted;

  public boolean isBuildCompleted()
  {
      return buildCompleted;
  }

  public interface BuildListener
  {
      public abstract void treeFinished(DBMetaObjects model);
  }

  public void run()
  {
      buildCompleted = false;
      build = true;
  Connection connection = null;
    try
    {
        connection = ConnectionUtil.getConnection(login);
        md = connection.getMetaData();
        //System.out.println("getCatalog():"+connection.getCatalog());
        build();
        if(build)
            buildListener.treeFinished(this);
    }
    catch (Exception ex)
    {
        ex.printStackTrace();
    }
    finally
    {
        try
        {
            if(connection != null)
                connection.close();
        }
        catch (SQLException ex1){}
    }
  }

  ObjectNode dataBase;
  Vector schemas;
  Vector allTables = new Vector();
  Vector treeModelListeners;
  BuildListener buildListener;
  DatabaseMetaData md;
  Login login;

  public Vector getAllTables(){ return allTables;}
  public String[] getTableColumns(String name)
  {
      Enumeration enumeration = allTables.elements();
      while(enumeration.hasMoreElements())
      {
          ObjectNode on = (ObjectNode)enumeration.nextElement();
          if(on.getName().equals(name))
          {
              ObjectNode[] oa= on.children();
              if(oa != null)
              {
                  String[] ret = new String[oa.length];
                  for(int i = 0; i < ret.length; i++)
                      ret[i] = oa[i].getName();

                 return ret;
              }
          }
      }

      return null;
  }

  public DBMetaObjects(Login login, BuildListener buildListener)
  throws Exception
  {
    this.dataBase = new ObjectNode(null, login.getUrl(), TYPE_DATABASE, null, null);
    treeModelListeners = new Vector();
    this.buildListener = buildListener;
    this.login = login;
  }

  public void stopBuild()
  {
      System.out.println("stopBuild()");
      build = false;
  }

  protected void build()
  throws Exception
  {
    ResultSet rsSch = md.getSchemas();

    this.schemas = processRs(rsSch, "TABLE_SCHEM", null, dataBase, TYPE_SCHEMA);
    if(schemas.size() > 0)
        dataBase.setChildren(schemas);

    rsSch.close();
    rsSch = null;
    int lim = schemas.size() == 0 ? 1 : schemas.size();

    for(int i = 0; i < lim && build; i++)
    {
      ObjectNode parentOfTable = schemas.size() == 0 ? null : (ObjectNode)schemas.elementAt(i);
      String currSchema = parentOfTable == null ? null : parentOfTable.getName();
      ResultSet rsTab = md.getTables(null, currSchema, null, null);
      Vector tables = processRs(rsTab, "TABLE_NAME", "TABLE_TYPE", parentOfTable, TYPE_TABLE);
      rsTab.close();
      rsTab = null;
      allTables.addAll(tables);
      if(parentOfTable != null)
          parentOfTable.setChildren(tables);
      for(int j = 0; j < tables.size() && build; j++)
      {
        ObjectNode parentOfColumn = (ObjectNode)tables.elementAt(j);
        String currTable = parentOfColumn.getName();
        String currTableType = parentOfColumn.getDatabaseType();
        //if(!"TABLE".equalsIgnoreCase(currTableType))
          //continue;
        Controller.trace(5, "-->" + parentOfColumn);
        //System.out.println("\t" + currTableType);
        ResultSet rsCol = md.getColumns(null,currSchema,currTable,null);
        Vector columns = processRs(rsCol, "COLUMN_NAME", "TYPE_NAME", parentOfColumn, TYPE_COLUMN);
        rsCol.close();
        rsCol = null;
        parentOfColumn.setChildren(columns);
        //Vector types = processRs(rsCol, );
        for(int k = 0; k < columns.size() && build; k++)
        {
          String currColumn = columns.elementAt(k).toString();
          //String currType = types.elementAt(k).toString();
          Controller.trace(5, "   -->" + currColumn);

          //System.out.println("\t" + currType);
        }
      }
      if(schemas.size() == 0)
        dataBase.setChildren(tables);
    }
    buildCompleted = true;
  }

  private Vector processRs(ResultSet rs, String key, String typeKey, ObjectNode parent, String type)
  throws Exception
  {
    Vector ret = new Vector();
    while(rs.next() && build)
    {
      String name = rs.getObject(key).toString();
      String databaseType = typeKey == null ? null : rs.getObject(typeKey).toString();
      ObjectNode on = new ObjectNode(parent, name, type, databaseType, null);
      if(key.equals("COLUMN_NAME"))
      {
        String size = rs.getObject("COLUMN_SIZE").toString();
        ObjectNode ty = new ObjectNode(on, size, TYPE_TYPE, databaseType, null);
        Vector vc = new Vector();
        vc.add(ty);
        on.setChildren(vc);
      }
      ret.add(on);
    }
    return ret;
  }

  public Object getRoot()
  {
    return dataBase;
  }

  public Object getChild(Object parent, int index)
  {
    if(!(parent instanceof ObjectNode))
      throw new RuntimeException("unexpected object of " + parent.getClass() + " passed");
    ObjectNode on = (ObjectNode)parent;
    return on.getChild(index);
  }

  public int getChildCount(Object parent)
  {
    if(!(parent instanceof ObjectNode))
      throw new RuntimeException("unexpected object of " + parent.getClass() + " passed");
    ObjectNode on = (ObjectNode)parent;
    return on.getChildCount();
  }

  public boolean isLeaf(Object node)
  {
    if(!(node instanceof ObjectNode))
      throw new RuntimeException("unexpected object of " + node.getClass() + " passed");
    ObjectNode on = (ObjectNode)node;
    return on.getChildCount() == 0;
  }

  public void valueForPathChanged(TreePath parm1, Object parm2)
  {
    System.out.println("Method valueForPathChanged() not yet implemented.");
  }
  public int getIndexOfChild(Object parent, Object child)
  {
    if(!(parent instanceof ObjectNode))
      throw new RuntimeException("unexpected object of " + parent.getClass() + " passed");
    if(!(child instanceof ObjectNode))
      throw new RuntimeException("unexpected object of " + child.getClass() + " passed");
    ObjectNode onp = (ObjectNode)parent;
    ObjectNode onc = (ObjectNode)child;
    ObjectNode[] ch = onp.children();
    for(int i = 0; i < ch.length; i++)
    {
      if(ch[i].equals(child))
        return i;
    }
    return -1;
  }

  public void addTreeModelListener(TreeModelListener tml)
  {
    //System.out.println("Method addTreeModelListener() called with:" + tml);
    //treeModelListeners.add(tml);
  }
  public void removeTreeModelListener(TreeModelListener tml)
  {
    System.out.println("Method removeTreeModelListener()");
    treeModelListeners.remove(tml);
  }

  public class ObjectNode
  {
    private ObjectNode parent;
    private String name;
    private String type;
    private String dataBaseType;
    private Vector children;

    private ObjectNode(ObjectNode parent, String name, String type, String dataBaseType, Vector children)
    {
      this.parent = parent;
      this.name = name;
      this.type = type;
      this.dataBaseType = dataBaseType;
      this.children = children;
    }
    public String toString()
    {
      if("type".equalsIgnoreCase(type))
        return dataBaseType + " size:" + name;
      return  name;
    }

    public boolean equals(Object obj)
    {
      if(!(obj instanceof ObjectNode))
      {
        return false;
      }
      ObjectNode on = (ObjectNode)obj;
      return on.name.equals(this.name) && on.type == this.type;
    }

    private void setChildren(Vector children)
    {
      this.children = children;
    }

    private ObjectNode[] children()
    {
      ObjectNode[] ret = new ObjectNode[children.size()];
      children.copyInto(ret);
      return ret;
    }

    public ObjectNode getChild(int index)
    {
      if(children != null && children.size() > index)
        return (ObjectNode)children.elementAt(index);
      return null;
    }

    public int getChildCount()
    {
      return children != null ? children.size() : 0;
    }

    public String getName()
    {
      return name;
    }

    public String getDatabaseType()
    {
      return dataBaseType;
    }

    public String getType()
    {
      return type;
    }

  }

  public TreeCellRenderer createCellRenderer()
  {
      return new MyRenderer(Constants.ICON_PATH);
  }
  ///-----
  class MyRenderer extends DefaultTreeCellRenderer
  {
    String gifDir;

    public MyRenderer(String gifDir)
    {
      this.gifDir = gifDir;
    }

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        String t = "attention";
        if((value instanceof DBMetaObjects.ObjectNode))
        {
            DBMetaObjects.ObjectNode on = (DBMetaObjects.
                ObjectNode) value;
            t = on.getType();
        }
        java.net.URL url = getClass().getResource(gifDir + t + ".gif");
        if(url != null)
        {
          setIcon(new ImageIcon(url));
        }
            setToolTipText("" + tree.getPathForRow(row));
        return this;
    }

  }


}
