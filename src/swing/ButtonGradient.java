/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author adam
 */
public class ButtonGradient extends JButton {

    private Color color1 = Color.decode("#0099F7");
    private Color color2 = Color.decode("#F11712");
    private final Timer timer;
    private final Timer timerPressed;
    private float alpha = 0.3f;
    private boolean mouseOver;
    private boolean pressed;
    private Point pressedlocation;
    private float pressedSize;
    private float sizeSpeed=2f;
    private float alphaPressed = 0.5f;

    public ButtonGradient() {
        setContentAreaFilled(false);
        setForeground(Color.white);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 20, 10, 20));

        // create Action
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                timer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                timer.start();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressedSize=0;
                alphaPressed = 0.5f;
                pressed = true;
                pressedlocation = e.getPoint();
                timerPressed.setDelay(0);
                timerPressed.start();
            }

        });

        //timer 1
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mouseOver) {
                    if (alpha < 0.6f) {
                        alpha += 0.05f;
                        repaint();
                    } else {
                        alpha = 0.6f;
                        timer.stop();
                        repaint();
                    }
                } else {
                    if (alpha > 0.3f) {
                        alpha -= 0.05f;
                        repaint();
                    } else {
                        alpha = 0.3f;
                        timer.stop();
                        repaint();
                    }

                }
            }
        });

        //timer 2
        timerPressed = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressedSize += sizeSpeed;
                if (alphaPressed <= 0) {
                    pressed = false;
                    timerPressed.stop();
                } else {
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphic) {
        int width = getWidth();
        int height = getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gd2 = img.createGraphics();
        gd2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // create Color
        GradientPaint grad = new GradientPaint(0, 0, color1, width, 0, color2);
        gd2.setPaint(grad);
        gd2.fillRoundRect(0, 0, width, height, height, height);

        // Add method Style
        createStyle(gd2);
        if (pressed) {
            paintPressed(gd2);
        }
        gd2.dispose();
        graphic.drawImage(img, 0, 0, null);
        super.paintComponent(graphic);
    }

    private void createStyle(Graphics2D gd2) {
        gd2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        int width = getWidth();
        int height = getHeight();
        GradientPaint grad = new GradientPaint(0, 0, Color.WHITE, 0, height, new Color(255, 255, 255, 60));
        gd2.setPaint(grad);
        Path2D.Float f = new Path2D.Float();
        f.moveTo(0, 0);
        int control1 = height + height / 2;
        f.curveTo(0, 0, width / 2, control1, width, 0);
        gd2.fill(f);
    }

    private void paintPressed(Graphics2D gd2) {
        if (pressedlocation.x - (pressedSize / 2) < 0 && pressedlocation.x + (pressedSize / 2) > getWidth()) {
            timerPressed.setDelay(20);
            alphaPressed -= 0.05f;
            if (alphaPressed < 0) {
                alphaPressed = 0;
            }
        }
        gd2.setColor(Color.WHITE);
        gd2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alphaPressed));
        float x = pressedlocation.x - (pressedSize / 2);
        float y = pressedlocation.y - (pressedSize / 2);
        gd2.fillOval((int) x, (int) y, (int) pressedSize, (int) pressedSize);
    }

}
