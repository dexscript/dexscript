# empty

```dexscript
await {}
```

* cases
    * size
        * 0

# await_consumer

```dexscript
await {
   case AA(): string {
       return 'hello'
   }
}
```

* cases
    * size
        * 1
    * [0]
        * identifier
            * "AA"

# await_producer

```dexscript
await {
   case res := <-a {
   }
}
```

* cases
    * size
        * 1
    * [0]
        * consumeStmt
            * "res := <-a"

# await_exit

```dexscript
await {
    exit!
}
```

* cases
    * size
        * 1
    * [0]
        * "exit!"

# await_multiple_cases

```dexscript
await {
   case AA(): string {
       return 'hello'
   }
   case res := <-a {
   }
   exit!
}
```

* cases
    * size
        * 3

# recover_from_invalid_statement

```dexscript
await {
   case AA(): string {
       return 'hello'
   }??
   case res := <-a {
   }
   exit!
}
```

```dexscript
await {
   case AA(): string {
       return 'hello'
   }<error/>??
   case res := <-a {
   }
   exit!
}
```


