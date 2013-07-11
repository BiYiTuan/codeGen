/*
 * Copyright 2013 elfkingw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Created on 2013-7-9

package org.richie.codeGen.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;

import org.richie.codeGen.core.exception.CGException;
import org.richie.codeGen.core.model.Column;
import org.richie.codeGen.core.model.Table;
import org.richie.codeGen.database.Constants;
import org.richie.codeGen.database.DatabaseReader;
import org.richie.codeGen.database.ReaderFactory;

/**
 * @author elfkingw
 */
public class CodeGenMainUI extends JFrame implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTree             tree;
    private List<Table>       tableList;
    private JPanel            westPanel;            // 左边面板
    private JTabbedPane       centerPanel;          // 中间面板
    private JScrollPane       treePanel;            // 树面板
    private GenAndPreviewUI   genAndPreviewPanel;   // 生成代码面板
    private JTextField filterField;

    private JMenuItem         openPdmFileItem;      // pdm文件菜单项
    private JMenuItem         miAbout;              // 关于菜单项

    public CodeGenMainUI(){
        initlize();
    }

    private void initlize() {
        setBounds(120, 80, 1000, 550);
        setDefaultCloseOperation(3);
        setLayout(new BorderLayout(5, 5));
        // 初始化MenuBar
        initMenuBar();
        add(getWestPanel(), BorderLayout.WEST);
        add(getCenterPanel(), BorderLayout.CENTER);
        setTitle("代码生成器");
    }

    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        menuBar.add(fileMenu);
        openPdmFileItem = new JMenuItem("打开dpm");
        fileMenu.add(openPdmFileItem);
        JMenu mnHelp = new JMenu("帮助");
        menuBar.add(mnHelp);
        miAbout = new JMenuItem("关于");
        mnHelp.add(miAbout);
        miAbout.addActionListener(this);
        openPdmFileItem.addActionListener(this);
        this.setJMenuBar(menuBar);
    }

    public JPanel getWestPanel() {
        if (westPanel == null) {
            westPanel = new JPanel();
            westPanel.setLayout(new BorderLayout());
            JLabel filterLable = new JLabel("过滤");
            JPanel filterPanel = new JPanel();
            filterPanel.add(filterLable);
            filterField =new JTextField(15);
            Document doc = filterField.getDocument(); 
            doc.addDocumentListener(new TextDocumentListener());
            filterPanel.add(filterField);
            filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            westPanel.setPreferredSize(new Dimension(200, 650));// 关键代码,设置JPanel的大
            westPanel.add(filterPanel, BorderLayout.NORTH);
            westPanel.add(getTreepanel(), BorderLayout.CENTER);
        }
        return westPanel;
    }
    

    public JTabbedPane getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JTabbedPane();
            centerPanel.add(getGenAndPreviewPanel(), "代码生成区");
        }
        return centerPanel;
    }

    public GenAndPreviewUI getGenAndPreviewPanel() {
        if (genAndPreviewPanel == null) {
            genAndPreviewPanel = new GenAndPreviewUI(this);
        }
        return genAndPreviewPanel;
    }

    public JScrollPane getTreepanel() {
        if (treePanel == null) {
            treePanel = new JScrollPane();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("请导入文件");
            tree = new JTree(root);
            treePanel.setViewportView(tree);
        }
        return treePanel;
    }

    private class TextDocumentListener implements DocumentListener{

        /* (non-Javadoc)
         * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
         */
        @Override
        public void insertUpdate(DocumentEvent e) {
            processChange(e);
        }

        /* (non-Javadoc)
         * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
         */
        @Override
        public void removeUpdate(DocumentEvent e) {
            processChange(e);
        }

        /* (non-Javadoc)
         * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
         */
        @Override
        public void changedUpdate(DocumentEvent e) {
            processChange(e);
        }
        private void processChange(DocumentEvent e){
            Document doc = e.getDocument();  
            try {
                String s = doc.getText(0, doc.getLength());
                filterTree(s);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            } 
        }
        
    }
    private void initTree(String filePath) {
        try {
            DatabaseReader reader = ReaderFactory.getReaderInstance(Constants.DATABASE_READER_TYPE_PDM,
                                                                    Constants.DATABASE_TYPE_MYSQL, filePath);
            tableList = reader.getTables();
            tree = initTreeData(tableList);
            treePanel.setViewportView(tree);
            getTreepanel().updateUI();
        } catch (CGException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filterTree(String filterStr){
        if(filterStr == null || filterStr.equals("")){
            tree = initTreeData(tableList);
            treePanel.setViewportView(tree);
            getTreepanel().updateUI();
        }
        if(filterStr!= null && filterStr.length()<=1){
            return;
        }
        List<Table> list = new ArrayList<Table>();
        for (Table table : tableList) {
            if(table.getTableName().toLowerCase().contains(filterStr.toLowerCase())
                    ||table.getTableCode().toLowerCase().contains(filterStr.toLowerCase())){
                list.add(table);
            }
        }
        tree = initTreeData(list);
        treePanel.setViewportView(tree);
        getTreepanel().updateUI();
        
    }

    /**
     * @param list
     * @return
     */
    private JTree initTreeData(List<Table> list) {
        TableTreeNode root = null;
        if (list == null || list.size() == 0) {
            root = new TableTreeNode("请导入文件");
        } else {
            root = new TableTreeNode(list.get(0).getDataBaseName() + ":" + list.get(0).getDataBaseCode());
            for (Table table : list) {
                list.get(0).getDataBaseName();
                TableTreeNode node = new TableTreeNode(table);
                root.add(node);
                List<Column> columnList = table.getFieldList();
                for (Column column : columnList) {
                    TableTreeNode columnNode = new TableTreeNode(column);
                    node.add(columnNode);
                }
            }
        }
        JTree newTree = new JTree(root);
        newTree.setCellRenderer(new TableTreeRender());
        return newTree;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openPdmFileItem) {
            JFileChooser jfc = new JFileChooser();// 文件选择器
            jfc.setCurrentDirectory(new File("d:\\"));// 文件选择器的初始目录定为d盘
            jfc.setFileSelectionMode(0);// 设定只能选择到文件
            jfc.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    if (f.getName().endsWith(".pdm") || f.isDirectory()) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return "pdm文件(*.pdm)";
                }
            });
            int state = jfc.showOpenDialog(null);
            if (state == 1) {
                return;
            } else {
                File f = jfc.getSelectedFile();
                initTree(f.getAbsolutePath());
            }
        } else if (e.getSource() == miAbout) {
            JOptionPane.showMessageDialog(this, "elfkingw版权所有  elfkingw@gmail.com", "提示",
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CodeGenMainUI ui = new CodeGenMainUI();
        ui.setVisible(true);
        // ui.pack();
    }
}
