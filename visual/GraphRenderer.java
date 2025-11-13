package visual;

import model.Edge;
import model.Graph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GraphRenderer extends JPanel {

    private final Graph graph;
    private final Set<Edge> mstEdges;
    private final Edge removed;
    private final Edge replacement;
    private final Map<Integer, Point> positions = new HashMap<>();
    private final String title;

    public GraphRenderer(Graph graph,
            Collection<Edge> mstEdges,
            Edge removed,
            Edge replacement,
            String title,
            int width,
            int height) {
        this.graph = graph;
        this.mstEdges = new HashSet<>(mstEdges);
        this.removed = removed;
        this.replacement = replacement;
        this.title = title;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(width, height));
        computeNodePositions(width, height);
    }

    private void computeNodePositions(int w, int h) {
        int n = graph.n;
        int radius = Math.min(w, h) / 2 - 60;
        int cx = w / 2;
        int cy = h / 2;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = cx + (int) (radius * Math.cos(angle));
            int y = cy + (int) (radius * Math.sin(angle));
            positions.put(i, new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // title
        if (title != null && !title.isEmpty()) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            g2.drawString(title, 20, 25);
        }

        // 1. all edges
        g2.setStroke(new BasicStroke(1.0f));
        g2.setColor(Color.LIGHT_GRAY);
        for (Edge e : graph.edges) {
            drawEdge(g2, e, String.valueOf(e.w));
        }

        // 2. MST edges
        g2.setStroke(new BasicStroke(3.0f));
        g2.setColor(new Color(70, 70, 70));
        for (Edge e : mstEdges) {
            drawEdge(g2, e, null); // weight already drawn above
        }

        // 3. removed edge in red
        if (removed != null) {
            g2.setStroke(new BasicStroke(4.0f));
            g2.setColor(Color.RED);
            drawEdge(g2, removed, null);
        }

        // 4. replacement edge in green
        if (replacement != null) {
            g2.setStroke(new BasicStroke(4.0f));
            g2.setColor(new Color(0, 150, 0));
            drawEdge(g2, replacement, null);
        }

        // 5. nodes
        for (int v = 0; v < graph.n; v++) {
            drawNode(g2, v);
        }

        g2.dispose();
    }

    private void drawEdge(Graphics2D g2, Edge e, String weightLabel) {
        Point p1 = positions.get(e.u);
        Point p2 = positions.get(e.v);
        if (p1 == null || p2 == null)
            return;

        g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));

        if (weightLabel != null) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            int mx = (p1.x + p2.x) / 2;
            int my = (p1.y + p2.y) / 2;
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(weightLabel, mx + 4, my - 4);
        }
    }

    private void drawNode(Graphics2D g2, int v) {
        Point p = positions.get(v);
        int r = 16;
        int x = p.x - r / 2;
        int y = p.y - r / 2;

        g2.setColor(new Color(30, 144, 255));
        g2.fill(new Ellipse2D.Double(x, y, r, r));

        g2.setColor(Color.BLACK);
        g2.draw(new Ellipse2D.Double(x, y, r, r));

        String label = String.valueOf(v);
        FontMetrics fm = g2.getFontMetrics();
        int tx = p.x - fm.stringWidth(label) / 2;
        int ty = p.y - r;
        g2.drawString(label, tx, ty);
    }

    /** Render one stage to PNG file. */
    public static void saveStageImage(
            Graph graph,
            Collection<Edge> mstEdges,
            Edge removed,
            Edge replacement,
            String title,
            Path outputDir,
            String fileName,
            int width,
            int height) throws IOException {
        Files.createDirectories(outputDir);
        GraphRenderer panel = new GraphRenderer(graph, mstEdges, removed, replacement,
                title, width, height);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        panel.setSize(width, height);
        panel.paint(g2);
        g2.dispose();

        Path file = outputDir.resolve(fileName);
        ImageIO.write(img, "png", file.toFile());
        System.out.println("Saved image: " + file.toAbsolutePath());
    }

    public static void showInWindow(Graph graph,
            Collection<Edge> mstEdges,
            Edge removed,
            Edge replacement,
            String title,
            int width,
            int height) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new GraphRenderer(graph, mstEdges, removed, replacement,
                    title, width, height));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
