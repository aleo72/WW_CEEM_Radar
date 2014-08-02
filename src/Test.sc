val list = List(1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 1, 1, 2, 2, 3, 4)



def pack[A](ls: List[A]): List[List[A]] = {
  if (ls.isEmpty) List(List())
  else {
    val (packed, next) = ls span( _ == ls.head)
    if (next == Nil) List(packed)
    else packed :: pack(next)
  }
}

pack(list)

List