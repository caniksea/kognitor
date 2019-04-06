val l = List(1, 2, 3)
val s = List(2, 4, 6)

val q = l.map(z => z * 2)

val w = List(l, s)

w.flatten

w.flatMap(x => x)