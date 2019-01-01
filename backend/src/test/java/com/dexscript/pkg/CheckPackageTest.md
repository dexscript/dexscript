# no_error

```dexscript
// /pkg1/__spi__.ds
interface :: {
}
```

# syntax_error

```dexscript
// /pkg1/__spi__.ds
interface :: {
}
```

```dexscript
// /pkg1/1.ds
f Hello() {
    World()
}
```

# referenced_not_existing_function

```dexscript
// /pkg1/__spi__.ds
interface :: {
}
```

```dexscript
// /pkg1/1.ds
function Hello() {
    World()
}
```

# reference_function_defined_by_global_spi

```dexscript
// /pkg1/__spi__.ds
interface :: {
    World()
}
```

```dexscript
// /pkg1/1.ds
function Hello() {
    World()
}
```

# global_spi_must_be_defined_in_spi_file

```dexscript
// /pkg1/__spi__.ds
interface abc {
}
```

```dexscript
// /pkg1/123.ds
interface :: {
}
```

# can_not_define_type_with_same_name

```dexscript
// /pkg1/__spi__.ds
interface :: {
}
```

```dexscript
// /pkg1/123.ds
interface abc {
}
```

```dexscript
// /pkg1/456.ds
interface abc {
}
```

# can_not_define_same_name_function_in_different_file

```dexscript
// /pkg1/__spi__.ds
interface :: {
}
```

```dexscript
// /pkg1/123.ds
function abc() {
}
```

```dexscript
// /pkg1/456.ds
function abc(arg0: int64) {
}
```

# context_type_is_added_to_function_signature

```dexscript
// /pkg1/__spi__.ds
interface :: {
}

interface $ {
    GetPid(): string
}
```

```dexscript
// /pkg1/123.ds
function Hello() {
   World($=new Context())
}
function World() {
}
function Context() {
}
```

# global_spi_reference_later_defined_types

```dexscript
// /pkg1/__spi__.ds
interface :: {
    New__(class: 'SomeClass'): SomeInf
}

interface SomeInf {
    toString(): string
}
```

```dexscript
// /pkg1/123.ds
function Hello() {
   new SomeClass().toString()
}
```
