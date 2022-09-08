case class Rectangle(width: Int, height: Int):
    val area = width * height

val rectA = Rectangle(4, 8)
val rectB = rectA.copy(width = rectA.width * 2)
rectB.area

{
sealed trait Shape
case class Rectangle(width: Int, height: Int) extends Shape
case class Circle(radius: Int) extends Shape

val someRect: Circle = Circle(5)
val someShape: Shape = someRect

val someShapeArea = someShape match
    case Rectangle(w, h) => w * h
    case Circle(r)        => r * r * 3.14

val isSomeShapeCircle = someShape match
    case circle: Circle => true
    case _              => false
}

// Model the actions a user can do with a channel such as
// subscribe, unsubscribe, post a message:

case class Channel(name: String)

sealed trait Action
case class Subscribe(channel: Channel) extends Action
case class Unsubscribe(channel: Channel) extends Action
case class PostMessage(channel: Channel, message: String) extends Action


// Enums are the same as sealead trait with its companion object keeping
// its values with an array 'values' and a method 'valueOf'.

enum PrimaryColor:
    case Red, Blue, Green

def isSeenByBlindPeople(color: PrimaryColor) =
    color match
        case PrimaryColor.Red   => false
        case PrimaryColor.Blue  => true
        case PrimaryColor.Green => false
    
PrimaryColor.values
PrimaryColor.valueOf("Green")


// 'Set' game modeling
{
case class Card(shape: Shape, number: Number, color: Color, shading: Shading)

enum Shape:
    case Diamond, Squiggle, Oval

enum Color:
    case Red, Green, Purple

enum Shading:
    case Open, Striped, Solid

enum Number:
    case One, Two, Three

def isValidSet(card1: Card, card2: Card, card3: Card): Boolean =
    checkProperty[Shape](card1.shape, card2.shape, card3.shape) &&
    checkProperty[Color](card1.color, card2.color, card3.color) &&
    checkProperty[Shading](card1.shading, card2.shading, card3.shading) &&
    checkProperty[Number](card1.number, card2.number, card3.number)

def checkProperty[A](prop1: A, prop2: A, prop3: A) =
    def allSame = 
        prop1 == prop2 && prop1 == prop3
    def allDifferent =
        prop1 != prop2 &&
        prop1 != prop3 &&
        prop2 != prop3
    allSame || allDifferent
}

// Advice for modeling:
// 
// > Identify the concept (nouns) that you are intersted in
// 
// > Identify the relations between them:
//      > Does a concept belongs to another one?
//          > "A rectangle has a width and has a height"
//          > "A 'post message' has a channel and a message"
//      > Does a concept generalize another one?
//          > "A shape can either be a rectangle or a circle"
//          > "A subscription is a possible action"
// 
// > Translate each concept into a type definition:
//      > Concepts belonging to others become parameters of case classes
//      > Concepts generalizing others become sealed traits
// 
// > Check that you can construct meaningful values from your model:
//      > Check that you can not construct nonsensical values
//        from your model
// 
// > Sometimes you need several iterations of domain model and logic
//   implementation to find the right solution
