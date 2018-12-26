# empty

```dexscript
 function hello() {}
```

```dexscript
function hello() {}
```

* identifier
    * "hello"
* sig
    * "()"
* blk
    * "{}"

# no_space_between_function_keyword_and_identifier

```dexscript
functionhello() {}
```

```dexscript
<unmatched>functionhello() {}</unmatched>
```

# one_argument

```dexscript
function hello(msg:string) {
}
```

* identifier
    * "hello"
* sig
    * params
        * [0]
            * paramName
                * "msg"

# missing_left_paren

```dexscript
function hello ) {
```

```dexscript
<unmatched>function hello ) {</unmatched>
```

# missing_function_keyword

```dexscript
example function hello () {
}
```

```dexscript
<unmatched>example function hello () {
}</unmatched>
```

# missing_left_brace

```dexscript
function hello ()
}
```

* blk
    * matched
        * false



