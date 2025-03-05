package WorldSimulation.Tools;

import java.awt.*;

public class Img {
    private String emoji;
    private Color color;
    public Img(String emoji, Color color) {
        this.emoji = emoji;
        this.color = color;
    }
    public Img() {
        this.emoji = "";
        this.color = Color.WHITE;
    }
    public String getName() {
        return emoji;
    }
    public Color getColor() {
        return color;
    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Img other = (Img) obj;
        return emoji.equals(other.emoji) && color == other.color;
    }
}
