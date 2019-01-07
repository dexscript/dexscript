# valid_index_set

```dexscript
function Hello() {
    world := new World()
    world['a'] = 'b'
}

function World() {
    await {
    case set(index: string, value: string) {
    }}
}
```

# invalid_index_set

```dexscript
function Hello() {
    world := new World()
    world['a'] = 'b'
}

function World() {
}
```

# valid_field_set

```dexscript
function Hello() {
    world := new World()
    world.a = 'b'
}

function World() {
    await {
    case setA(value: string) {
    }}
}
```

# invalid_field_set

```dexscript
function Hello() {
    world := new World()
    world.a = 'b'
}

function World() {
}
```

# valid_assignment

```dexscript
function Hello() {
   var i: int32
   i = 100
}
```

# invalid_assignment

```dexscript
function Hello() {
   var i: int64
   i = 'hello'
}
```