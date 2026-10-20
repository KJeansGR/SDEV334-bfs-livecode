import java.util.*;
public class Search {


    public static void main(String[] args){
                
        Set<Location> hasBeenQueued = new HashSet<>();

        Location a = new Location(7,8);
        Location location = new Location(7,8);
        
        hasBeenQueued.add(a);
        System.out.println(hasBeenQueued.contains(location));

        char[][] maze1 = {
            {'o', 'o', 'o', 'o', 'c', 'w', 'c', 'o'},
            {'w', 'o', 'o', 'w', 'w', 'c', 'w', 'o'},
            {'o', 'o', 'o', 'o', 'R', 'w', 'o', 'o'},
            {'o', 'o', 'w', 'w', 'w', 'o', 'o', 'o'},
            {'o', 'o', 'o', 'o', 'c', 'o', 'o', 'o'}
        };
        char[][] maze2 = {
            {'o', 'o', 'o', 'o', 'c', 'w', 'c', 'o'},
            {'w', 'o', 'o', 'w', 'w', 'c', 'w', 'o'},
            {'o', 'o', 'o', 'o', 'R', 'w', 'o', 'o'},
            {'o', 'o', 'w', 'w', 'w', 'o', 'o', 'o'},
            {'o', 'o', 'o', 'o', 'c', 'o', 'o', 'o'}
        };
        nearestCheese(maze1);
        // System.out.println(hasBeenQueued.contains(b));
        // System.out.println(hasBeenQueued.contains(c));

    }
     /**
     * TODO: FIX COMMENTS TO SHOW RETURN IS NOW A LIST
     * Finds the location of the nearest reachable cheese from the rat's position.
     * Returns a 2-element int array: [row, col] of the closest 'c'. If there are multiple
     * cheeses that are tied for the same shortest distance to reach, return
     * any one of them.
     * 
     * 'R' - the rat's starting position (exactly one)
     * 'o' - open space the rat can walk on
     * 'w' - wall the rat cannot pass through
     * 'c' - cheese that the rat wants to reach
     * 
     * If no rat is found, throws EscapedRatException.
     * If more than one rat is found, throws CrowdedMazeException.
     * If no cheese is reachable, throws HungryRatException
     *
     * oooocwco
     * woowwcwo
     * ooooRwoo
     * oowwwooo
     * oooocooo
     *
     * The method will return [0,4] as the nearest cheese.
     *
     * @param maze 2D char array representing the maze
     * @return int[] location of the closest cheese in row, column format
     * @throws EscapedRatException if there is no rat in the maze
     * @throws CrowdedMazeException if there is more than one rat in the maze
     * @throws HungryRatException if there is no reachable cheese
     */
    public static List<Location> nearestCheese(char[][] maze) throws EscapedRatException, CrowdedMazeException, HungryRatException {
        
        Location start = ratLocation(maze);
        Map<Location, Location> prevs = new HashMap<>();

        Set<Location> hasBeenQueued = new HashSet<>();
        Queue<Location> queue = new ArrayDeque<>();
        
        queue.offer(start);
        hasBeenQueued.add(start);

        while(!queue.isEmpty()){
            Location cur = queue.remove();
            // if( maze[cur.row()][cur.column()] != 'w'){
            //     continue;
            // }

            if(maze[cur.row()][cur.column()] == 'c'){

                Location step = cur;
                List<Location> path = new LinkedList<>();

                while(step != null){
                    System.out.println(step);
                    path.addFirst(step);
                    step = prevs.get(step);
                }
                return path;

            }
            
            List<Location> neighbors = possibleMoves(maze, cur);

            for(Location n : neighbors){
                if(hasBeenQueued.add(n)){
                    prevs.putIfAbsent(n, cur);
                    queue.offer(n);
                }
            }
            
        }
        throw new HungryRatException();
    }


    /**
     * 
     * @param maze
     * @param current
     * @return
     */
    public static List<Location> possibleMoves(char[][] maze, Location current){
        List<Location> possible = new ArrayList<>();
        int[][] moves = new int[][]{
            {-1,0},
            {1,0},
            {0,1},
            {0,-1},
        };

        for(int[] move: moves){
            Location possibleMove = new Location(current.row()    + move[0],
                                                current.column() + move[1]);

            if( possibleMove.row() >= 0           &&
                possibleMove.row() < maze.length &&
                possibleMove.column() >= 0       &&
                possibleMove.column() < maze[0].length &&
           maze[possibleMove.row()][possibleMove.column()] != 'w'
            ){
                    possible.add(possibleMove);
            }
        }

        return possible;
    }

    /**
     * 
     * @param maze
     * @return
     */
    public static Location ratLocation(char[][] maze){
        Location location = null;

        for(int r = 0; r <maze.length; r++){
            for(int c = 0; c <maze[r].length; c++){
                if(maze[r][c] == 'R'){
                    if(location == null){
                        location = new Location(r,c);
                    }
                    else
                        throw new CrowdedMazeException();
                }
            }
        }
        
        if(location == null){
            throw new EscapedRatException();
        }
        return location;
    }
}