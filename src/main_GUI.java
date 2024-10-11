import processing.core.PApplet;
import javax.swing.JOptionPane;

public class main_GUI extends PApplet
{
    private GameOfLife game;
    private int windowsize = 600;
    private int col_count;
    private int row_count; 
    private int cell_size; 
    private int chosenFrameRate; // Frame rate for cells (different from the buttons)
    private boolean isRunning = false;
    private int offsetX;
    private int offsetY;
    public static void main(String[] args)
    {
        PApplet.main("GUI");
    }

    public void settings()
    {
        size(windowsize, windowsize + 50);
    }

    public void setup()
    {
        promptForValues();
        frameRate(60);
        calculateCellSizeAndOffsets();
        game = new GameOfLife(col_count, row_count);
        textAlign(CENTER, CENTER);
    }

    public void draw()
    {
        background(255); 
        int[][] board = game.getBoard();
        int y = 0;
        while (y < row_count)
        {
            int x = 0;
            while (x < col_count)
            {
                if (board[x][y] == 1)
                    fill(0); 
                else
                    fill(255); 
                stroke(0); 
                rect(offsetX + x * cell_size, offsetY + y * cell_size, cell_size, cell_size);
                x++;
            }
            y++;
        }
        drawButtons(); 

        if (isRunning)
        {
            if (frameCount % (60 / chosenFrameRate) == 0) // Update frame rate based on user's chosenFrameRate
                game.updateBoard();
        }
    }

    private void promptForValues()
    {
        while (true)
        {
            String inputCols = JOptionPane.showInputDialog("Enter the number of columns (5 to 100):");
            if (inputCols == null) // User clicked "Cancel"
                System.exit(0);
            try
            {
                col_count = Integer.parseInt(inputCols);
                if (col_count >= 5 && col_count <= 100)
                    break;
            }
            catch (NumberFormatException e) {}
            JOptionPane.showMessageDialog(null, "Please enter a valid number between 5 and 100.");
        }

        while (true)
        {
            String inputRate = JOptionPane.showInputDialog("Enter the updating rate (1 to 60):");
            if (inputRate == null)
                System.exit(0);
            try
            {
                chosenFrameRate = Integer.parseInt(inputRate);
                if (chosenFrameRate >= 1 && chosenFrameRate <= 60)
                    break;
            }
            catch (NumberFormatException e) {}
            JOptionPane.showMessageDialog(null, "Please enter a valid number between 1 and 60.");
        }

        row_count = col_count;
        calculateCellSizeAndOffsets();
    }

    private void calculateCellSizeAndOffsets()
    {
        cell_size = windowsize / col_count; // Calculate cell size
        // Calculate offsets for centering
        offsetX = (windowsize - (cell_size * col_count)) / 2;
        offsetY = (windowsize - (cell_size * row_count)) / 2;
    }

    private void drawButtons()
    {
        int buttonWidth = 100;
        int buttonHeight = 30;
        
        int totalButtonWidth = buttonWidth * 2 + 10; // with 10 pixel spacing between buttons
        int startButtonX = (windowsize / 2) - (totalButtonWidth / 2);
        int randomButtonX = startButtonX + buttonWidth + 10;
        int resetButtonX = windowsize - buttonWidth - 10; //  Bottom right corner, 10 pixels from the edge
        int clearButtonX = 10; // Bottom left corner, 10 pixels from the edge
        int clearButtonY = windowsize + 10;
    
        // Start/Pause button
        if (isRunning)
        {
            fill(200, 100, 100); 
            rect(startButtonX, windowsize + 10, buttonWidth, buttonHeight);
            fill(255);
            textSize(14);
            text("Pause", startButtonX + buttonWidth / 2, windowsize + 25);
        }
        else
        {
            fill(100, 200, 100); 
            rect(startButtonX, windowsize + 10, buttonWidth, buttonHeight);
            fill(255);
            textSize(14);
            text("Start", startButtonX + buttonWidth / 2, windowsize + 25);
        }
    
        // Random Pattern button
        fill(100, 100, 200); 
        rect(randomButtonX, windowsize + 10, buttonWidth, buttonHeight);
        fill(255);
        textSize(14);
        text("Random", randomButtonX + buttonWidth / 2, windowsize + 25);
        
        // Reset button (bottom right)
        fill(200, 200, 100); 
        rect(resetButtonX, windowsize + 10, buttonWidth, buttonHeight);
        fill(255);
        textSize(14);
        text("Reset", resetButtonX + buttonWidth / 2, windowsize + 25);

        // Clear button (bottom left)
        fill(150, 150, 255); 
        rect(clearButtonX, clearButtonY, buttonWidth, buttonHeight);
        fill(255);
        textSize(14);
        text("Clear", clearButtonX + buttonWidth / 2, clearButtonY + buttonHeight / 2);
    }

    // Adding "Action Listeners" for the buttons using the placements chosen for the buttons
    public void mousePressed()
    {
        if (mouseY < windowsize)
        {
            int x = (mouseX - offsetX) / cell_size;
            int y = (mouseY - offsetY) / cell_size;
            if (x >= 0 && x < col_count && y >= 0 && y < row_count)
            {
                int[][] board = game.getBoard();
                board[x][y] = (board[x][y] == 1) ? 0 : 1; 
            }
        }
        else
        {
            int buttonWidth = 100;
            int buttonHeight = 30;
            int totalButtonWidth = buttonWidth * 2 + 10;
            int startButtonX = (windowsize / 2) - (totalButtonWidth / 2);
            int randomButtonX = startButtonX + buttonWidth + 10;
            int resetButtonX = windowsize - buttonWidth - 10;

            // "Start/Pause" button hitbox
            if (mouseX >= startButtonX && mouseX <= startButtonX + buttonWidth &&
                mouseY >= windowsize + 10 && mouseY <= windowsize + 10 + buttonHeight)
            {
                isRunning = !isRunning; 
            } 
            // "Random" button hitbox
            else if (mouseX >= randomButtonX && mouseX <= randomButtonX + buttonWidth &&
                     mouseY >= windowsize + 10 && mouseY <= windowsize + 10 + buttonHeight)
            {
                game.randomBoard(); 
            }
            // "Reset" button hitbox
            else if (mouseX >= resetButtonX && mouseX <= resetButtonX + buttonWidth &&
                     mouseY >= windowsize + 10 && mouseY <= windowsize + 10 + buttonHeight)
            {
                promptForValues();
                game = new GameOfLife(col_count, row_count);
                isRunning = false;
            }
            // "Clear" button hitbox
            else if (mouseX >= 10 && mouseX <= 10 + buttonWidth &&
                     mouseY >= windowsize + 10 && mouseY <= windowsize + 10 + buttonHeight)
            {
                game.resetBoard();
            }
        }
    }
}
