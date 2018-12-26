# one_actor

```dexscript
function hello() {
}
```

* topLevelDecls
    * size
        * 1
    * [0]
        * actor
            * identifier
                * "hello"

# two_actors

```dexscript
function hello() {
}

function world() {
}
```

* topLevelDecls
    * size
        * 2
    * [0]
        * actor
            * identifier
                * "hello"
    * [1]
        * actor
            * identifier
                * "world"

# skip_garbage

```dexscript
function hello() {
}
xxx
function world() {
}
```

* topLevelDecls
    * size
        * 2
    * [0]
        * actor
            * blk
            * body
                * syntaxError
                    * toString

# leaving_src_unparsed

```dexscript
f hello() {}
```

```dexscript
<error/>f hello() {}
```

