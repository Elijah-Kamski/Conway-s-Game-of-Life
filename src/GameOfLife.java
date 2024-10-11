public class GameOfLife
{
    private int[][] board;
    private int col_count;
    private int row_count;

    public GameOfLife(int col_count, int row_count)
    {
        this.col_count = col_count;
        this.row_count = row_count;
        board = new int[col_count][row_count];
    }

    public void randomBoard()
    {
        int y = 0;
        while (y < row_count)
        {
            int x = 0;
            while (x < col_count)
            {
                board[x][y] = (int) (Math.random() * 2); // Randomly 0 or 1 (dead or alive)
                x++;
            }
            y++;
        }
    }

    public void resetBoard()
    {
        board = new int[col_count][row_count];
    }

    private int countLiveNeighbors(int x, int y)
    {
        int liveNeighbors = 0;

        int i = -1;
        while (i <= 1)
        {
            int j = -1;
            while (j <= 1)
            {
                if (i == 0 && j == 0) 
                {
                    j++;
                    continue; // Skip the cell itself
                }
                int neighborX = (x + i + col_count) % col_count;
                int neighborY = (y + j + row_count) % row_count;
                liveNeighbors += board[neighborX][neighborY];
                j++;
            }
            i++;
        }
        return (liveNeighbors);
    }

    public void updateBoard()
    {
        int[][] newBoard = new int[col_count][row_count];

        int y = 0;
        while (y < row_count)
        {
            int x = 0;
            while (x < col_count)
            {
                int liveNeighbors = countLiveNeighbors(x, y);

                if (board[x][y] == 1)
                {
                    if (liveNeighbors < 2 || liveNeighbors > 3)
                    {
                        newBoard[x][y] = 0;
                    }
                    else
                    {
                        newBoard[x][y] = 1;
                    }
                }
                else
                {
                    if (liveNeighbors == 3)
                    {
                        newBoard[x][y] = 1;
                    }
                    else
                    {
                        newBoard[x][y] = 0;
                    }
                }
                x++;
            }
            y++;
        }
        board = newBoard;
    }

    public int[][] getBoard()
    {
        return (board);
    }
}
