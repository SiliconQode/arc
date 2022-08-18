/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
 * Copyright (c) 2017-2021 Empirilytics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.siliconcode.arc.disharmonies.experiment

import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.util.List

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ExperimentGenerator {

    private JFrame frmRcbdPatternExperiment
    private JTextField textField
    private JTextField textField_1
    private JTable table

    private String[][] locations = [
            ["Adapter-Command", "C:\\experiments\\DesignPatterns\\instances\\Adapter-Command"],
            ["Abstract Factory", "C:\\experiments\\DesignPatterns\\instances\\Abstract Factory"],
            ["Composite", "C:\\experiments\\DesignPatterns\\instances\\Composite"],
            ["Chain of Responsibility", "C:\\experiments\\DesignPatterns\\instances\\Chain of Responsibility"],
            ["Decorator", "C:\\experiments\\DesignPatterns\\instances\\Decorator"],
            ["Facade", "C:\\experiments\\DesignPatterns\\instances\\Facade"],
            ["Factory Method", "C:\\experiments\\DesignPatterns\\instances\\Factory Method"],
            ["Flyweight", "C:\\experiments\\DesignPatterns\\instances\\Flyweight"],
            ["Mediator", "C:\\experiments\\DesignPatterns\\instances\\Mediator"],
            ["Observer", "C:\\experiments\\DesignPatterns\\instances\\Observer"],
            ["Prototype", "C:\\experiments\\DesignPatterns\\instances\\Prototype"],
            ["Proxy", "C:\\experiments\\DesignPatterns\\instances\\Proxy"],
            ["Singleton", "C:\\experiments\\DesignPatterns\\instances\\Singleton"],
            ["State", "C:\\experiments\\DesignPatterns\\instances\\Singleton"],
            ["Strategy", "C:\\experiments\\DesignPatterns\\instances\\Strategy"],
            ["Template Method", "C:\\experiments\\DesignPatterns\\instances\\Template Method"],
            ["Visitor", "C:\\experiments\\DesignPatterns\\instances\\Visitor"]]
    private JSpinner spinner

    /**
     * Launch the application.
     */
    static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            public void run()
            {
                try
                {
                    ExperimentGenerator window = new ExperimentGenerator()
                    window.frmRcbdPatternExperiment.setVisible(true)

                }
                catch (Exception e)
                {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     * Create the application.
     */
    ExperimentGenerator()
    {
        initialize()
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frmRcbdPatternExperiment = new JFrame()
        frmRcbdPatternExperiment.setTitle("RCBD Pattern Experiment Generator")
        frmRcbdPatternExperiment.setBounds(100, 100, 450, 467)
        frmRcbdPatternExperiment.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frmRcbdPatternExperiment.getContentPane().setLayout(new BorderLayout(0, 0))

        JPanel panel_1 = new JPanel()
        frmRcbdPatternExperiment.getContentPane().add(panel_1, BorderLayout.NORTH)
        GridBagLayout gbl_panel_1 = new GridBagLayout()
        gbl_panel_1.columnWidths = [0, 352, 0, 0]
        gbl_panel_1.rowHeights = [0, 0, 0, 0]
        gbl_panel_1.columnWeights = [0.0, 1.0, 0.0, Double.MIN_VALUE]
        gbl_panel_1.rowWeights = [0.0, 0.0, 0.0, Double.MIN_VALUE]
        panel_1.setLayout(gbl_panel_1)

        JLabel lblInstanceBaseDir = new JLabel("Instance Base Dir:")
        GridBagConstraints gbc_lblInstanceBaseDir = new GridBagConstraints()
        gbc_lblInstanceBaseDir.anchor = GridBagConstraints.EAST
        gbc_lblInstanceBaseDir.insets = new Insets(0, 0, 5, 5)
        gbc_lblInstanceBaseDir.gridx = 0
        gbc_lblInstanceBaseDir.gridy = 0
        panel_1.add(lblInstanceBaseDir, gbc_lblInstanceBaseDir)

        textField_1 = new JTextField()
        GridBagConstraints gbc_textField_1 = new GridBagConstraints()
        gbc_textField_1.insets = new Insets(0, 0, 5, 5)
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL
        gbc_textField_1.gridx = 1
        gbc_textField_1.gridy = 0
        panel_1.add(textField_1, gbc_textField_1)
        textField_1.setColumns(10)

        JButton button = new JButton("...")
        button.addActionListener(new ActionListener() {
            void actionPerformed(ActionEvent e)
            {
                JFileChooser jfc = new JFileChooser()
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
                int retVal = jfc.showOpenDialog(frmRcbdPatternExperiment)
                if (retVal == JFileChooser.APPROVE_OPTION)
                {
                    textField_1.setText(jfc.getSelectedFile().getPath())
                }
            }
        })
        GridBagConstraints gbc_button = new GridBagConstraints()
        gbc_button.insets = new Insets(0, 0, 5, 0)
        gbc_button.gridx = 2
        gbc_button.gridy = 0
        panel_1.add(button, gbc_button)

        JLabel lblNewLabel = new JLabel("Save Location:")
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints()
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5)
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST
        gbc_lblNewLabel.gridx = 0
        gbc_lblNewLabel.gridy = 1
        panel_1.add(lblNewLabel, gbc_lblNewLabel)

        textField = new JTextField()
        GridBagConstraints gbc_textField = new GridBagConstraints()
        gbc_textField.insets = new Insets(0, 0, 5, 5)
        gbc_textField.fill = GridBagConstraints.HORIZONTAL
        gbc_textField.gridx = 1
        gbc_textField.gridy = 1
        panel_1.add(textField, gbc_textField)
        textField.setColumns(10)

        JButton btnNewButton_1 = new JButton("...")
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0)
            {
                JFileChooser jfc = new JFileChooser()
                int ret = jfc.showSaveDialog(frmRcbdPatternExperiment)
                if (ret == JFileChooser.APPROVE_OPTION)
                    textField.setText(jfc.getSelectedFile().getPath())
            }
        })
        GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints()
        gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0)
        gbc_btnNewButton_1.gridx = 2
        gbc_btnNewButton_1.gridy = 1
        panel_1.add(btnNewButton_1, gbc_btnNewButton_1)

        JLabel lblReplications = new JLabel("Replications:")
        GridBagConstraints gbc_lblReplications = new GridBagConstraints()
        gbc_lblReplications.anchor = GridBagConstraints.EAST
        gbc_lblReplications.insets = new Insets(0, 0, 0, 5)
        gbc_lblReplications.gridx = 0
        gbc_lblReplications.gridy = 2
        panel_1.add(lblReplications, gbc_lblReplications)

        spinner = new JSpinner()
        spinner.setPreferredSize(new Dimension(50, 28))
        spinner.setModel(new SpinnerNumberModel(1, 1, 100, 1))
        GridBagConstraints gbc_spinner = new GridBagConstraints()
        gbc_spinner.anchor = GridBagConstraints.WEST
        gbc_spinner.insets = new Insets(0, 0, 0, 5)
        gbc_spinner.gridx = 1
        gbc_spinner.gridy = 2
        panel_1.add(spinner, gbc_spinner)

        JScrollPane scrollPane = new JScrollPane()
        frmRcbdPatternExperiment.getContentPane().add(scrollPane, BorderLayout.CENTER)

        table = new JTable()
        table.setModel(new DefaultTableModel([], ["X", "Pattern Name", "Instance Count", "Max Reps", "Location"]) {
            Class[] columnTypes = [String.class, String.class, Integer.class, Integer.class, String.class,
                    Object.class]

            Class getColumnClass(int columnIndex)
            {
                return columnTypes[columnIndex]
            }

            boolean[] columnEditables = [true, false, false, false, false]

            boolean isCellEditable(int row, int column)
            {
                return columnEditables[column]
            }
        })
        table.getColumnModel().getColumn(0).setResizable(false)
        table.getColumnModel().getColumn(0).setPreferredWidth(15)
        table.getColumnModel().getColumn(1).setPreferredWidth(150)
        scrollPane.setViewportView(table)

        JPanel panel = new JPanel()
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT)
        frmRcbdPatternExperiment.getContentPane().add(panel, BorderLayout.SOUTH)
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2))

        JButton btnNewButton = new JButton("Exit")
        btnNewButton.addActionListener(new ActionListener() {
            void actionPerformed(ActionEvent e)
            {
                System.exit(0)
            }
        })
        btnNewButton.setMnemonic('x')
        panel.add(btnNewButton)

        JButton btnNewButton_2 = new JButton("Generate")
        btnNewButton_2.addActionListener(new ActionListener() {
            void actionPerformed(ActionEvent e)
            {
                try
                {
                    generate()
                }
                catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace()
                }
            }
        })
        btnNewButton_2.setMnemonic('G')
        panel.add(btnNewButton_2)

        try
        {
            fillTable()
        }
        catch (IOException e)
        {

        }

        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace()
        }
    }

    /**
     * @throws IOException
     */
    protected void generate() throws IOException
    {
        List<Treatment> treats = new ArrayList<>()
        String[] treatments = ["DIPG", "IIPG", "DISG", "IISG", "DEPG", "IEPG", "DESG", "IESG"]
        String location = textField.getText()

        String[][] selectedPatterns = getSelectedPatterns()
        int reps = getReplications()
        System.out.println("Replications: " + reps)

        for (int i = 0; i < selectedPatterns.length; i++)
        {
            String pattern = selectedPatterns[i][0]
            Map<Integer, List<String>> instances = randomInstanceSelection(reps, selectedPatterns[i][1],
                    treatments.length)
            treats.addAll(randomAssignTreatments(pattern, reps, instances, treatments))
        }

        Collections.shuffle(treats)

        writeTreatmentList(treats, location)
    }

    /**
     * @return
     */
    private String[][] getSelectedPatterns()
    {
        DefaultTableModel model = (DefaultTableModel) table.getModel()
        int count = 0
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if (((String) model.getValueAt(i, 0)).equals("S"))
                count++
        }

        String[][] retVal = new String[count][2]
        int currIndex = 0
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if (((String) model.getValueAt(i, 0)).equals("S"))
            {
                retVal[currIndex][0] = ((String) model.getValueAt(i, 1))
                retVal[currIndex][1] = ((String) model.getValueAt(i, 4))
                currIndex++
            }
        }

        return retVal
    }

    private int getReplications()
    {
        DefaultTableModel model = (DefaultTableModel) table.getModel()
        int count = 0
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if (((String) model.getValueAt(i, 0)).equals("S"))
                count++
        }

        List<Integer> reps = new ArrayList<>()
        int currIndex = 0
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if (((String) model.getValueAt(i, 0)).equals("S"))
            {
                reps.add((Integer) model.getValueAt(i, 3))
                currIndex++
            }
        }
        System.out.println("Reps size: " + reps.size())
        return Math.min(Collections.min(reps), (Integer) spinner.getValue())
    }

    private void fillTable() throws IOException
    {
        for (String[] loc : locations)
        {
            Path path = Paths.get(loc[1])
            DirectoryStream<Path> contents = Files.newDirectoryStream(path)
            Iterator<Path> iter = contents.iterator()
            int count = 0

            List<Path> toDelete = new ArrayList<>()
            while (iter.hasNext())
            {
                Path p = iter.next()
                if (Files.isDirectory(p))
                {
                    DirectoryStream<Path> st = Files.newDirectoryStream(p)
                    Iterator<Path> i = st.iterator()
                    if (!i.hasNext())
                    {
                        toDelete.add(p)
                        continue
                    }
                    count++
                }
            }

            for (Path p : toDelete)
            {
                Files.delete(p)
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel()
            Object[] o = ["", loc[0], count, count / 8, loc[1]]
            model.addRow(o)
        }
    }

    /**
     * @param string
     * @param length
     * @return
     * @throws IOException
     */
    private Map<Integer, List<String>> randomInstanceSelection(int reps, String string, int length) throws IOException
    {
        Map<Integer, List<String>> dirContents = new HashMap<>()
        List<String> contents = new ArrayList<>()
        Path path = Paths.get(string)
        DirectoryStream<Path> stream = Files.newDirectoryStream(path)
        Iterator<Path> iter = stream.iterator()
        while (iter.hasNext())
        {
            Path p = iter.next()
            if (Files.isDirectory(p))
            {
                contents.add(p.toString())
            }
        }

        Collections.shuffle(contents)

        for (int i = 0; i < reps; i++)
            dirContents.put(i, contents.subList(i * length, i * length + length))
        return dirContents
    }

    /**
     * @param instances
     * @param treatments
     * @return
     */
    private List<Treatment> randomAssignTreatments(String pattern, int rep, Map<Integer, List<String>> instances,
            String[] treatments)
    {
        List<Treatment> retVal = new ArrayList<>()
        List<String> treats = Arrays.asList(treatments)
        Collections.shuffle(treats)
        for (int k = 0; k < rep; k++)
        {
            for (int i = 0; i < instances.get(k).size(); i += treatments.length)
            {
                for (int j = 0; j < treatments.length; j++)
                {
                    retVal.add(new Treatment(pattern, k, instances.get(k).get(i + j), treats.get(j)))
                }
            }
        }

        return retVal
    }

    /**
     * @param treats
     * @throws IOException
     */
    private void writeTreatmentList(List<Treatment> treats, String location) throws IOException
    {
        Path path = Paths.get(location)
        Files.deleteIfExists(path)

        Files.createFile(path)
        PrintWriter pw
        try
        {
            pw = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE))
            pw.println("Pattern,Rep,InstanceLoc,Injection")
            System.out.println("Number of Treatments: " + treats.size())
            for (Treatment t : treats)
            {
                pw.println(t.toString())
            }
        }
        catch (IOException e)
        {
            e.printStackTrace()
        } finally {
            pw?.close()
        }
    }

    class Treatment {
        String pattern
        String instanceLoc
        String treatment
        int rep

        Treatment(String pattern, int rep, String instanceLoc, String treatment)
        {
            this.pattern = pattern
            this.instanceLoc = instanceLoc
            this.treatment = treatment
            this.rep = rep
        }

        String toString()
        {
            return String.format("%s,%d,%s,%s", pattern, rep, instanceLoc, treatment)
        }
    }
}
