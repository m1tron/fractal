# Fractal

## Environment & Tools

| Program        |   Version    |
|----------------|:------------:| 
| `MacOS`        |   **14.0**   |
| `IntelliJ IDEA` | **2022.3.3** |
| `Java JDK`     |  **17 LTS**  |
| `Git`          |  **2.40.1**  |
| `Maven`        |  **3.9.1**   |

## Background
This was originally a school assignment to create a Java Swing MVC application. Much of what is left here is from the original hand-in but with some pruning and updates. The original requirements are listed below (since they shine some light on design choices).

1. The MVC design pattern, by separating the view and model, and routing any input or communication between the two through a controller.
2. Java Swing GUI, by implementing at least three different Swing components, and one layout.
3. Java Streams API, by implementing at least one non-trivial stream.

## Introduction
In the book "Introduction to Java Programming" (2010) by David Liang, a demonstration of how a snowflake can be drawn recursively was shown. This principle of recursively drawing fractals were attractive and perhaps a more general implementation could be done where the user can input the parameters of the fractal and have the program draw it. The idea was to use this as a basis for the future sprite generation in an Asteroids game.

### Design choices
To implement a program where the user can draw fractals a few design choices need to be made:
1. How should the user input be gathered as a basis for the fractal?
2. How does the MVC pattern fit into the program?
3. To which extent should parameters be configurable by the user? Does the user get to modify the base polygon or also the recursive blueprint? How many points should be in the blueprint? 

For the first point (1) several paths was considered. Adjusting constants with sliders seem like a straight forward implementation, but it also somewhat limits the range of inputs and also would require at least six sliders (two per coordinate) for a basic snowflake. Additional sliders would also be needed to generated at runtime if more points are needed. Text boxes for entering values manually could in theory be used, but with the same issue if extending the program. Given the focus on GUI, implementing a graphical interface where the user can click and drag out a graphical representation of values seems like a good start. Actually some inspiration were found in the course book where Horstmann demonstrates how mouse listeners can be used to spawn rectangles on a JComponent. It seems like a good start point to build from. 

The MVC-pattern (2) tries to separate concerns of view and model. In this program, the user will ultimately paint directly on a view, and then have the result drawn in another view. Basically, the model would only add a redundant intermediary step of data storage if it was shoe-horned in, since the actual state of things are already what is rendered. Thankfully, in the context of reusing any user construct at a later stage, it would make sense to let the model handle loading and saving to storage such that the MVC model can still be leveraged. 

For point three (3), as a starting point, the user should be able to configure both things. The exact implementation will hopefully reveal itself naturally during implementation. 

### Fractal blueprint and recursion
To quickly touch upon what is meant when talking about this concept, the fractal blueprint is essentially a set of points (polyline) **P** = {A...B} that can be used to replace a line _AB_. By linear transformation, the polyline can be transformed to replace an arbitrary line, thus it is a blueprint. Recursion can be achieved by taking all segments of P, and again replacing them with the transformed polyline P.

### Class implementations
Moving on, multiple classes were implemented simultaneously with the first objective being to successfully draw a snowflake.

#### EditorView
This is the main class of the View. It is an extension of JFrame that will structure and show the GUI on instantiation. A main panel (JPanel) is first used to cover the whole frame. To separate the layout into output and input, any user input should reside on the right side, while to the left (or center), there should be a render-panel to render the fractal. To facilitate this, the BorderLayout is a good fit since it enables the division of the main panel into 5 different subsections of North, West, South, East and Center. For this use case, the render panel will be situated in the center, with user input panels residing to the East. This side panel can then implement a separate LayoutManager to organize its components. It should hold two EditorPanels, a recursion blueprint panel, a polygon panel. 

A panel to hold buttons was later added `buttonPanel` to hold buttons for pushing data from the editors to the renderer, and provide access to functionality for loading and saving constructs. Accessors for these buttons are provided such that the controller can stitch in listeners and provide functionality to them.


#### EditorPanel
The editor panel builds upon the work of Horstmann (2018-04-10 v01ch10 MouseComponent.java). The original program used a JComponent to let the user place, drag and remove squares. Instead of mirroring the key concepts, it was decided to extend the original functionality as an exercise. In the original implementation the squares are stored as an ArrayList<Rectangle2D>. No other data structure is needed in theory, since a points can be derived for a rectangle. An ArrayList also stores the elements in the order they are added. Together, these are the only requirements needed to extrapolate a polygon or a set of lines from the array that can then be used as either a polygon base, or a recursive blueprint. To visually communicate what the final product looks like to the user, the `paintComponent()` method was modified to draw either a polygon or lines over the squares. This works well for the intended purpose of letting the user draw simple polygon, even though it fails to solve the issue of only letting the user add new squares to the end of the array. A couple of public helper methods are constructed (`generatePoints()` and `generatePolygon()`) such that other parts of the program can access the finished constructs. `initSquares()` was then added to supply a base pattern to guide the user in the right direction. This will also speed up testing as data is already present for testing.

#### RenderPanel
The responsibility of the **RenderPanel** is to render a fractal using a polygon and a recursive blueprint. The panel stores two variables representing these states, `Polygon polygon` and `List<Point2D.Float> relativePoints`. Setting of these variables are the responsibility of the controller, and setters will be constructed for this purpose as `setPoly(Polygon p)` and `setRelativePoints(List<PointD.Float> p)`. 

`drawPoly(Graphics2D g)` is the main entrypoint for rendering the fractal and is invoked from inside `paintComponent()`. It deconstructs the polygon into points using by retrieving the properties `Point2d.xpoints` and `Point2d.ypoints`. By iterating over these coordinates and creating a new Point2D object for every pair of points, a list is compiled that can subsequently be used in conjunction with `drawRecursiveLine()` to recursively draw the polygon. To discuss the implementation of the recursive method, it is included here for clarity:
```java
public void drawRecursiveLine(Point2D.Float startPoint, Point2D.Float endPoint, int n, Graphics g){
        /* Recursively do this part until n = 0 */
        if(n > 0) {
            /* Substitute the startPoint to endPoint line with multiple lines calculated from relativePoints */
            var newPoints = new ArrayList<Point2D.Float>();
            newPoints.add(startPoint);
            relativePoints.forEach( p -> newPoints.add(calculateActualPoint(p.x, p.y, startPoint, endPoint)));
            newPoints.add(endPoint);

            /* Draw all new sub lines recursively */
            for (int i = 0; i < newPoints.size()-1; i++) {
                drawRecursiveLine(newPoints.get(i), newPoints.get(i+1), n-1, g);
            }
        }
        /* Draw the line normally when recursive depth is reached */
        else {
            g.drawLine((int) startPoint.x, (int) startPoint.y, (int) endPoint.x, (int) endPoint.y);
        }
    }
```
The arguments `startPoint` and `endPoint` represents the start and the end point of the line that is to be drawn. `n` is a counter for the recursion. When first called this value will match the constant `ViewConfig.RECURSIVE_DEPTH` which is 3 at the moment. `drawRecursiveLine()` is then divided into an if-else statement to control the recursion. When `n` reaches 0, the recursion ends and `Graphics2d.drawLine()` is used to draw the final line normally. Otherwise, a new point is generated for every point in `relativePoints` by calling `calculateActualPoint()`. Between each of these points, a line is drawn using `drawRecursiveLine()` again, which will either just draw the line if `n` reaches 0, or once again generate new points and repeat.

`calculateActualPoint(float p_x, float p_y, Point2D.Float start, Point2D.Float end)` takes the x and y values of a point p = (p_x, p_y) and uses them to calculate a new point using two other points `start` and `end`. A new vector is created from `start` to `end`, that is used to construct another vector, that is returned, by taking the dot product of that vector and `p` (notice that points are sometimes referred to as vectors when they are considered as such for mathematical context). The reason this works (without diving into linear algebra) can be illustrated with examples. If the point p(1,0) is supplied, the method will yield the point `end`, and p(0,0) gives `start`. p(0.5, 0) would yield the halfway point between `start` and `end`. Changing the other coordinate like p(0,1) would yield a point relative to `start` that is as equidistant from `start` as `end`, in the direction orthogonal from the direction `start` -> `end`. One could also imagine a placing a coordinate grid over start and end points where `start` lies in origin and `end` at (1,0). Then the point `p` can be directly represented by placing it on the grid. 

`calculateRelativePoint()` works similarly to `calculateActualPoint()` except that it does the inverse. To use the grid analogy, it places a grid over two points `start` and `end` at (0,0) and (1,0) respectively, and then reads the coordinates for a third point `p` and returns the new coordinates as a `Point2D.Float` object. This method is not used for rendering, but supplied publicly so that any users of the class may convert to relative coordinates if needed.

#### BlackPaddedPanel
When customizing the look of the other panels, experimenting with padding and background color, it was found that many of the settings were repeated. In an attempt to align with the DRY-principle, commonality between panels was refactored into a new class called `BlackPaddedPanel`. 

#### Model
The responsibility of the model in this implementation is to facilitate loading and saving of assets. Three methods are provided as `createSprite()`, `storeSprite()` and `readSprite()`. The `Sprite` class is defined in the model package and is a simple record class that stores the necessary components of a sprite, namely the polygon and recursive blueprint. It also implements the serializable interface to provide serialization capabilities. To store a sprite, the model utilizes a `FileOutputStream` wrapped inside an `ObjectOutputStream` to write sprite objects directly to file. The mirror approach is used for reading, but with input streams. A default filename is hardcoded as "sprite.obj" to work as a placeholder. Preferably in the future, the editor would read a manifest produced from a game, and let user choose from a list of filenames, so that they might be produced, and directly loaded into a game for testing.

#### SpriteEditorController
The controller constructs a view and a model and then proceeds to wire up the view with listeners. In total there are four buttons that are defined in the view. For each of the buttons the controller maintains a corresponding method to execute actions. They are `saveAction()`, `loadAction()`, `polyAction()` and `recursiveAction()`. The two first are pulls data from the view to the model and vice versa. The two latter pulls data from respective editor panel into the render panel. By having each individual component be modular like this and let a controller be responsible for intercommunication, loose coupling is achieved. The model, or any of the panels can be changed out at any time without affecting anything else, as long as the controller integrates the new part.

The bindings of actions to buttons are done by setting listeners on buttons. The `addActionListener()` method is used for this, and as an example `(e)-> saveAction()` is passed as argument. This is possible because `ActionListner` is a functional interface, and we can implement anonymously by passing a lambda. In this case, the corresponding action is passed.


### Alternative Approaches
Many alternative solutions were considered. Both editors panels are now derived from the same class, using switch statement on a boolean to decide behavior. This is not extensible and common behaviour could be extracted into an abstract superclass. 

The helper methods in render panel could also be refactored into a static utility class to further improve extensibility. For example, providing more than one render panel might be a future idea, to simultaneously show fractals at different depths of recursion. Then it would make sense to put common mathematical methods inside a utility class.

The model should also be extended to handle input and output in a more satisfactory way. Like touched upon earlier it would be nice if a game using this editor could supply a set of strings for filenames so that generating sets of compatible sprites could be done batch-wise. Also, while writing java objects to files directly might seem easy at first, its is complicated by the fact that any serialized object can only be deserialized into an object of the same name and package, meaning that any program using the sprites would need the editor as a dependency to deserialize. An easier approach would therefore be to write and read from json files. After all, a sprite only consists of a small set of coordinates. 

## Personal Reflections
Learning and reasoning about the MVC pattern was probably the biggest hurdle, tightly followed by fading math skills in relation to 2d-transformations. It was an interesting assignment to stitch together the different components into a working program. At first, I went heavy on constructing new classes for every panel, but it quickly becomes cumbersome to navigate the program, and it is important to find the sweat spot. In the final solution only unique constructs have their own classes, with most of the programmatically generated UI residing in `EditorView` exclusively. Different approaches have their different sweat spots when it comes to adding layers of abstraction. Large projects necessarily need more layers of abstraction to break down components, while smaller projects can get away with less abstraction.