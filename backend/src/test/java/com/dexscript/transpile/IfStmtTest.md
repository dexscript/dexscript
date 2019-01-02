# only_if

```dexscript
function Hello(): string {
   if 1 == 1 {
       return 'hello'
   }
   return 'world'
}
```

* "hello"

# if_then_else

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

# if_then_else_if

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

# statements_after_if

```dexscript
function Hello(): string {
   var msg: string
   if (1 == 0) {
       msg = 'hello'
   } else if (1 == 1) {
       msg = 'world'
   }   return msg
}
```

* "world"



