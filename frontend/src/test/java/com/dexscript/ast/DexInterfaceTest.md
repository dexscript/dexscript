# empty

```dexscript
interface Duck {}
```

* identifier
    * "Duck"

# global_spi

```dexscript
interface :: {}
```

* identifier
    * "::"

# no_space_between_interface_keyword_and_identifier

```dexscript
interfaceDuck {}
```

```dexscript
<unmatched>interfaceDuck {}</unmatched>
```

# no_space_between_identifier_and_left_brace

```dexscript
interface Duck{}
```

* identifier
    * "Duck"

# inf_method

```dexscript
interface Duck {
    Quack(): string
}
```

* methods
    * size
        * 1
    * [0]
        * "Quack(): string"
* functions
    * size
        * 0

# inf_function

```dexscript
interface Duck {
    ::Quack(): string
}
```

* methods
    * size
        * 0
* functions
    * size
        * 1
    * [0]
        * "::Quack(): string"

# inf_field

```dexscript
interface Duck {
    Name: string
}
```

* fields
    * size
        * 1
    * [0]
        * "Name: string"

# recover_invalid_inf_member_by_line_end

```dexscript
interface Duck {
    ??;
    Quack(): string
}
```

* methods
    * size
        * 1

# type_parameter

```dexscript
interface List {
   <T>: string
   Get__(index: int64): T
}
```

* typeParams
    * size
        * 1
    * [0]
        * `<T>: string`

# two_type_parameters

```dexscript
interface AnotherInf {
   <E1>: interface{};
   <E2>: interface{};
   Get__(index: '0', arg: E1)
   Get__(index: '1', arg: E2)
}
```

* typeParams
    * size
        * 2
    * [0]
        * `<E1>: interface{}`
    * [1]
        * `<E2>: interface{}`



