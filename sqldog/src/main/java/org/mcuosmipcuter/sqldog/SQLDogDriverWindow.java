package org.mcuosmipcuter.sqldog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.util.*;
import javax.swing.border.*;

public class SQLDogDriverWindow extends JFrame
{
    private java.io.File currDir;
    ////
    class MyCellRenderer extends JLabel implements ListCellRenderer {
        public MyCellRenderer() {
            setOpaque(true);
        }
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            String v = value.toString();
            String dv = v;
            final ImageIcon archIcon = new ImageIcon(getClass().getResource(SQLDogConstants.ICON_PATH + "arch.gif"));
            final ImageIcon dirIcon = new ImageIcon(getClass().getResource(SQLDogConstants.ICON_PATH + "dir.gif"));
            if(value.toString().startsWith("a:"))
            {
                this.setIcon(archIcon);
                dv =v.substring(2);
            }
            else if(value.toString().startsWith("d:"))
            {
                this.setIcon(dirIcon);
                dv =v.substring(2);
            }
            else
            {
                this.setIcon(null);
            }
            //setBorder(isSelected ? new LineBorder(list.getSelectionForeground()) :new LineBorder(list.getBackground()) );
            //setBackground(list.getBackground());
            setFont(list.getFont().deriveFont(Font.PLAIN));
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setText(dv);
            return this;
        }
    }
    MyCellRenderer cellRenderer = new MyCellRenderer();
    ////
    DefaultComboBoxModel list = new DefaultComboBoxModel(new Object[]{"one/two", "three/four", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
    ImageIcon imageIcon = new ImageIcon(getClass().getResource(SQLDogConstants.ICON_PATH + "sqldog.gif"));
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JButton jButtonClose = new JButton();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel5 = new JPanel();
  JButton jButtonMoveUp = new JButton();
  JButton jButtonMoveDown = new JButton();
  JButton jButtonRemove = new JButton();
  JPanel jPanel6 = new JPanel();
  JButton jButtonAdd = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JList jListLocations = new JList();
  JLabel jLabel1 = new JLabel();
  BorderLayout borderLayout2 = new BorderLayout();
  JLabel jLabel2 = new JLabel();
    GridLayout gridLayout2 = new GridLayout();
    Border border1;
    public SQLDogDriverWindow() throws HeadlessException
    {
        try {
          jbInit();
        }
        catch(Exception e) {
          e.printStackTrace();
        }
        jListLocations.setCellRenderer(cellRenderer);
        this.setIconImage(imageIcon.getImage());
    }
    public Vector getDriverLocations()
    {
        Vector v = new Vector();
        for(int i = 0; i < jListLocations.getModel().getSize(); i++)
            v.add(jListLocations.getModel().getElementAt(i));
        return v;
    }
    public void setDriverLocations(Vector driverLocations)
    {
        for(int i = 0; i < driverLocations.size(); i++)
        {
            String val = driverLocations.elementAt(i).toString();
            if(val.startsWith("a:"))
            {
                SQLDogConnectionUtil.addJarFile(val.substring(2));
            }
            if(val.startsWith("d:"))
            {
                SQLDogConnectionUtil.addExplDir(val.substring(2));
            }

        }
        list = new DefaultComboBoxModel(driverLocations);
        jListLocations.setModel(list);
    }
    public static void main(String[] args) throws HeadlessException
    {
        SQLDogDriverWindow SQLDogDriverWindow1 = new SQLDogDriverWindow();
        SQLDogDriverWindow1.pack();
        SQLDogDriverWindow1.setVisible(true);
    }
    private void jbInit() throws Exception {
      border1 = BorderFactory.createEmptyBorder(0,10,0,10);
        jPanel1.setLayout(gridLayout1);
      jPanel2.setBackground(new Color(187, 221, 228));
    jPanel2.setLayout(borderLayout1);
      jPanel3.setMinimumSize(new Dimension(100, 100));
    jPanel3.setOpaque(false);
    jPanel3.setPreferredSize(new Dimension(100, 40));
      jPanel4.setOpaque(false);
    jPanel4.setPreferredSize(new Dimension(100, 30));
    jPanel4.setLayout(borderLayout2);
    jButtonClose.setBorder(null);
    jButtonClose.setPreferredSize(new Dimension(65, 25));
    jButtonClose.setText("close");
    jButtonClose.addActionListener(new SQLDogDriverWindow_jButtonClose_actionAdapter(this));
        this.getContentPane().setBackground(new Color(187, 221, 228));
    this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    this.setResizable(false);
    this.setTitle("Drivers");
    jPanel5.setBackground(Color.pink);
        jPanel5.setBorder(border1);
    jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new Dimension(120, 200));
    jPanel5.setLayout(gridLayout2);
    jButtonMoveUp.setBorder(null);
    jButtonMoveUp.setPreferredSize(new Dimension(65, 25));
        jButtonMoveUp.setText("move up");
    jButtonMoveUp.addActionListener(new SQLDogDriverWindow_jButtonMoveUp_actionAdapter(this));
    jButtonMoveDown.setBorder(null);
    jButtonMoveDown.setPreferredSize(new Dimension(65, 25));
        jButtonMoveDown.setText("move down");
    jButtonMoveDown.addActionListener(new SQLDogDriverWindow_jButtonMoveDown_actionAdapter(this));
    jButtonRemove.setBorder(null);
    jButtonRemove.setPreferredSize(new Dimension(95, 25));
        jButtonRemove.setText("remove");
    jButtonRemove.addActionListener(new SQLDogDriverWindow_jButtonRemove_actionAdapter(this));
    jPanel6.setOpaque(false);
    jPanel6.setPreferredSize(new Dimension(30, 30));
    jPanel6.setRequestFocusEnabled(true);
    jButtonAdd.setBorder(null);
    jButtonAdd.setPreferredSize(new Dimension(65, 25));
        jButtonAdd.setText("add");
    jButtonAdd.addActionListener(new SQLDogDriverWindow_jButtonAdd_actionAdapter(this));
    jListLocations.setModel(list);
    jListLocations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1.setOpaque(true);
    jScrollPane1.setPreferredSize(new Dimension(504, 140));
    jScrollPane1.setRequestFocusEnabled(true);
    jLabel1.setDoubleBuffered(false);
    jLabel1.setPreferredSize(new Dimension(30, 15));
    jLabel1.setRequestFocusEnabled(true);
    jLabel1.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel1.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel1.setText("locations of jar files zip files or exploded directories containing " +
    "JDBC drivers:");
    borderLayout2.setHgap(1);
    borderLayout2.setVgap(6);
    jLabel2.setPreferredSize(new Dimension(30, 15));
    jLabel2.setText("");
    jPanel1.setOpaque(false);
    gridLayout2.setColumns(1);
        gridLayout2.setHgap(3);
        gridLayout2.setRows(5);
        gridLayout2.setVgap(16);
        this.getContentPane().add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jPanel5, BorderLayout.EAST);
    jPanel5.add(jButtonMoveUp, null);
        jPanel5.add(jButtonMoveDown, null);
    jPanel5.add(jButtonRemove, null);
    jPanel5.add(jButtonAdd, null);
    jPanel2.add(jPanel6, BorderLayout.WEST);
    jPanel2.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jListLocations, null);
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(jPanel3, null);
    jPanel3.add(jButtonClose, null);
    this.getContentPane().add(jPanel4, BorderLayout.NORTH);
    jPanel4.add(jLabel1, BorderLayout.CENTER);
    jPanel4.add(jLabel2,  BorderLayout.WEST);
    }

    private void moveListElement(int increment)
    {
        Object mover = jListLocations.getSelectedValue();
        int oldIndex = jListLocations.getSelectedIndex();
        int newIndex = oldIndex + increment;
        if(oldIndex < 0 || newIndex > jListLocations.getModel().getSize() - 1)
            return;
        if(newIndex < 0 || newIndex > jListLocations.getModel().getSize() - 1)
            return;

        list.removeElementAt(oldIndex);
        list.insertElementAt(mover, oldIndex + increment);
        jListLocations.setSelectedIndex(newIndex);
    }

  void jButtonRemove_actionPerformed(ActionEvent e) {
      int selectedIndex = jListLocations.getSelectedIndex();
      if(selectedIndex < 0)
          return;
      int res = JOptionPane.showConfirmDialog(this,
      "are you sure you want to remove '" + jListLocations.getSelectedValue() + "' ?",
      "connect",
      JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
            list.removeElementAt(selectedIndex);
  }

  void jButtonMoveUp_actionPerformed(ActionEvent e) {
      moveListElement(-1);
  }

  void jButtonMoveDown_actionPerformed(ActionEvent e) {
      moveListElement(1);
  }

  void jButtonAdd_actionPerformed(ActionEvent e) {

          JFileChooser chooser = new JFileChooser();
          FileFilter filter = new FileFilter()
          {
              public boolean accept(java.io.File f)
              {
                  return (f != null && f.isDirectory() || f.getName().endsWith(".jar") || f.getName().endsWith(".zip"));
              }

              public String getDescription()
              {
                  return "directories or jar/zip archives";
              }
          };

          chooser.setFileFilter(filter);
          if(currDir != null)
          {
              chooser.setCurrentDirectory(currDir);
          }
          final ImageIcon archIcon = new ImageIcon(getClass().getResource(SQLDogConstants.ICON_PATH + "arch.gif"));
          chooser.setFileView(new FileView(){
              public Icon getIcon(java.io.File f)
              {
                  if(f != null && (f.getName().endsWith(".jar") || f.getName().endsWith(".zip")))
                      return archIcon;
                  return null;
              }
          }
              );
          chooser.setDialogTitle("Load drivers from exploded directories or jar/zip archives");
          chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

          int returnVal = chooser.showDialog(this, "Load");
          if(returnVal == JFileChooser.APPROVE_OPTION)
          {
              String fullName = chooser.getCurrentDirectory() +
                  chooser.getSelectedFile().separator +
                  chooser.getSelectedFile().getName();
              SQLDogController.trace(4, "open file: " + fullName);
              if(chooser.getSelectedFile() != null)
              {
                  //System.out.println("isDirectory:"+chooser.getSelectedFile().isDirectory());
                  Vector newValues = new Vector();
                  for(int i = 0; i < jListLocations.getModel().getSize(); i++)
                  {
                      newValues.add(jListLocations.getModel().getElementAt(i));
                  }
                  String namePrefix = "";
                  if(chooser.getSelectedFile().isDirectory())
                  {
                      namePrefix = "d:";
                  }
                  else
                  {
                      namePrefix = "a:";
                  }
                  currDir = chooser.getCurrentDirectory();
                  if(newValues.contains(namePrefix + fullName))
                  {
                      JOptionPane.showMessageDialog(this, fullName + "\nis already contained in list!");
                      return;
                  }
                  newValues.add(namePrefix + fullName);
                  if(namePrefix.equals("d:"))
                      SQLDogConnectionUtil.addExplDir(fullName);
                  else
                      SQLDogConnectionUtil.addJarFile(fullName);

                  list = new DefaultComboBoxModel(newValues);
                  jListLocations.setModel(list);
                  jListLocations.setSelectedIndex(jListLocations.getModel().getSize() - 1);
              }

          }

  }

  void jButtonClose_actionPerformed(ActionEvent e) {
      this.setVisible(false);
  }

}

class SQLDogDriverWindow_jButtonRemove_actionAdapter implements java.awt.event.ActionListener {
  SQLDogDriverWindow adaptee;

  SQLDogDriverWindow_jButtonRemove_actionAdapter(SQLDogDriverWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonRemove_actionPerformed(e);
  }
}

class SQLDogDriverWindow_jButtonMoveUp_actionAdapter implements java.awt.event.ActionListener {
  SQLDogDriverWindow adaptee;

  SQLDogDriverWindow_jButtonMoveUp_actionAdapter(SQLDogDriverWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonMoveUp_actionPerformed(e);
  }
}

class SQLDogDriverWindow_jButtonMoveDown_actionAdapter implements java.awt.event.ActionListener {
  SQLDogDriverWindow adaptee;

  SQLDogDriverWindow_jButtonMoveDown_actionAdapter(SQLDogDriverWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonMoveDown_actionPerformed(e);
  }
}

class SQLDogDriverWindow_jButtonAdd_actionAdapter implements java.awt.event.ActionListener {
  SQLDogDriverWindow adaptee;

  SQLDogDriverWindow_jButtonAdd_actionAdapter(SQLDogDriverWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonAdd_actionPerformed(e);
  }
}

class SQLDogDriverWindow_jButtonClose_actionAdapter implements java.awt.event.ActionListener {
  SQLDogDriverWindow adaptee;

  SQLDogDriverWindow_jButtonClose_actionAdapter(SQLDogDriverWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonClose_actionPerformed(e);
  }
}