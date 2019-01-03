# only_if_without_await

```dexscript
function Hello(): string {
    if 1 == 1 {
        return 'hello'
    }
    return 'world'
}
```

* "hello"

# only_if_with_await

```dexscript
function Hello(): string {
    if 1 == 1 {
        val := new AsIs('hello')
        return <-val
    }
    return 'world'
}
function AsIs(i: string): string {
    return i
}
```

* "hello"


# if_then_else_without_await

```dexscript
function Hello(): string {
   if 1 == 0 {
       return 'hello'
   } else {
       return 'world'
   }
}
```

* "world"

# if_then_else_with_await

```dexscript
function Hello(): string {
    if 1 == 0 {
        val := new AsIs('hello')
        return <-val
    } else {
        return 'world'
    }
}
function AsIs(i: string): string {
    return i
}
```

* "world"

# if_then_else_if_without_await

```dexscript
function Hello(): string {
   if 1 == 0 {
       return 'hello'
   } else if 1 == 1 {
       return 'world'
   }
}
```

* "world"

# if_then_else_if_with_await

```dexscript
function Hello(): string {
    if 1 == 0 {
        val := new AsIs('hello')
        return <-val
    } else if 1 == 1 {
        return 'world'
    }
}
function AsIs(i: string): string {
    return i
}
```

* "world"

# statements_after_if_without_await

```dexscript
function Hello(): string {
    var msg: string
    if (1 == 0) {
        msg = 'hello'
    } else if (1 == 1) {
        msg = 'world'
    }
    return msg
}
```

* "world"

# statements_after_if_with_await

```dexscript
function Hello(): string {
    var msg: string
    if (1 == 0) {
        val := new AsIs('hello')
        msg = <-val
    } else if (1 == 1) {
        msg = 'world'
    }
    return msg
}
function AsIs(i: string): string {
    return i
}
```

* "world"



