import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class SURAJ {
    public static void main(String[] args) {
        createandShowGUI();
    }

    public static void createandShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));
        frame.setMinimumSize(new Dimension(600, 600));
        ImageIcon image = new ImageIcon("E:\\APP gui\\WhatsApp Image 2023-11-05 at 20.41.02_0c2cdcb6.jpg");
        Image scaledImage = image.getImage().getScaledInstance(frame.getWidth(), frame.getHeight() / 2, Image.SCALE_SMOOTH);

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(scaledImage, 0, 0, this);
            }
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        panel.add(Box.createVerticalGlue());

        JButton button1 = new JButton("Get locations");
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button1);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton button2 = new JButton("Get pricing according to a location");
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button2);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton button3 = new JButton("Exit");
        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button3);

        panel.add(Box.createVerticalGlue());
        frame.add(imagePanel);
        frame.add(panel);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetLocationGui();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetPriceGui();
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    public static void GetLocationGui() {
        JFrame locationFrame = new JFrame("Location Selection");
        locationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        locationFrame.setMinimumSize(new Dimension(600, 800));

        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        String[] locationOptions = {"Location 1", "Location 2", "Location 3"};
        JComboBox<String> locationDropdown = new JComboBox<>(locationOptions);
        locationDropdown.setPreferredSize(new Dimension(400, locationDropdown.getPreferredSize().height));

        // Create a "Fetch Location" button
        JButton fetchButton = new JButton("Fetch Location");
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeHttpGetRequest(locationDropdown);
            }
        });
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                locationFrame.dispose(); // Close the current location frame
                createandShowGUI(); // Return to the home page
            }
        });

        // Add components to the locationPanel
        locationPanel.add(locationDropdown);
        locationPanel.add(fetchButton);
        locationPanel.add(backButton);

        locationFrame.add(locationPanel);
        locationFrame.pack();
        locationFrame.setVisible(true);
    }
    public static void makeHttpGetRequest(JComboBox<String> locationDropdown) {
        try {
            String apiUrl = "http://127.0.0.1:8080/get_location_names";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                reader.close();

                // Parse the response and update the dropdown
                String[] locationOptions = response.toString().split(","); // Assuming data is comma-separated
                locationDropdown.removeAllItems();
                for (String option : locationOptions) {
                    locationDropdown.addItem(option.trim());
                }
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    public static void GetPriceGui() {
        JFrame priceFrame = new JFrame("Pricing Information");
        priceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        priceFrame.setSize(new Dimension(600, 600));
        //priceFrame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel pricePanel = new JPanel(new GridLayout(5,2));
        pricePanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding

        // Labels
        JLabel locationLabel = new JLabel("Location name:");
        JLabel areaLabel = new JLabel("Area (sqft):");
        JLabel bedroomsLabel = new JLabel("No of bedrooms:");
        JLabel washroomsLabel = new JLabel("No of washrooms:");

        // Text Fields
        JTextField locationField = new JTextField();
        JTextField areaField = new JTextField();
        JTextField bedroomsField = new JTextField();
        JTextField washroomsField = new JTextField();

        // Add labels and text fields to the panel
        pricePanel.add(locationLabel);
        pricePanel.add(locationField);
        pricePanel.add(areaLabel);
        pricePanel.add(areaField);
        pricePanel.add(bedroomsLabel);
        pricePanel.add(bedroomsField);
        pricePanel.add(washroomsLabel);
        pricePanel.add(washroomsField);

        // Create a button to perform some action (e.g., fetch pricing)
        JButton fetchPriceButton = new JButton("Fetch Pricing");
        fetchPriceButton.setBackground(new Color(51, 153, 255));
        fetchPriceButton.setForeground(Color.WHITE);
        fetchPriceButton.setFont(new Font("Arial", Font.BOLD, 14));
        fetchPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action to fetch pricing based on the input fields
                String location = locationField.getText();
                String area = areaField.getText();
                String bedrooms = bedroomsField.getText();
                String washrooms = washroomsField.getText();
                try {
                    makeHttpPostRequest(location, area, bedrooms, washrooms);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // You can make an HTTP request here to fetch pricing data
            }
        });

        pricePanel.add(fetchPriceButton);

        priceFrame.add(pricePanel);
        priceFrame.setVisible(true);


    }
    public static void makeHttpPostRequest(String location, String area, String bedrooms, String washrooms) throws IOException {
        String urlString = "http://localhost:8080/predict_home_price";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String requestBody = "total_sqft=" + area + "&location=" + location + "&bhk=" + bedrooms + "&bath=" + washrooms;
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] requestBytes = requestBody.getBytes("UTF-8");
            os.write(requestBytes, 0, requestBytes.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder responseBody = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line);
                }
                String a=responseBody.toString();
                Pattern pattern = Pattern.compile("\\d+\\.\\d+");

                // Create a Matcher for the input string
                Matcher matcher = pattern.matcher(a);

                List<String> matches = new ArrayList<>();

                // Find all matches
                while (matcher.find()) {
                    matches.add(matcher.group());
                }
                JOptionPane.showMessageDialog(null,"Estimated price: " + matches.get(0) +"L INR","Price info",JOptionPane.PLAIN_MESSAGE);
            }
        } else {
            System.out.println("Error: " + responseCode);
}
    }
}