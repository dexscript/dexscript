# assignable_to_same_structure

```dexscript
interface Hello {
   Action1(): string
   Action2(): string
}
```

```dexscript
interface World {
   Action1(): string
   Action2(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `Hello`      | `World`      |
| true       | `World`      | `Hello`      |
| false      | `World`      | `string`     |

# more_member

```dexscript
interface Hello {
   Action1(): string
}
```

```dexscript
interface World {
   Action1(): string
   Action2(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `Hello`      | `World`      |
| false      | `World`      | `Hello`      |

# argument_is_sub_type

```dexscript
interface Hello {
    Action1(msg: string)
}
```

```dexscript
interface World {
    Action1(msg: 'sun')
}
```


| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| false      | `Hello`      | `World`      |
| true       | `World`      | `Hello`      |

# ret_is_sub_type

```dexscript
interface Hello {
    Action1(): string
}
```

```dexscript
interface World {
    Action1(): 'sun'
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `Hello`      | `World`      |
| false      | `World`      | `Hello`      |

# implement_interface_by_define_function

```dexscript
interface SomeInf {
   SomeAction(): string
}
```

```dexscript
SomeAction(self: string): string
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| false      | `string`     | `SomeInf`    |
| true       | `SomeInf`    | `string`     |

# argument_is_super_type_can_still_implement

```dexscript
interface SomeInf {
    SomeAction(msg: 'sun')
}
```

```dexscript
SomeAction(self: int64, msg: string)
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| false      | `int64`     | `SomeInf`    |
| true       | `SomeInf`    | `int64`     |

# parameterized_type_with_incompatible_param

```dexscript
interface List {
   <T>: string
   Get__(index: int64): T
}
```

| equals | left           | right         |
| ---------- | ------------ | ------------ |
| true       | `undefined`    | `List<int64>`     |

# generic_interface

```dexscript
interface List {
   <T>: interface{}
   Get__(index: int64): T
}
```

```dexscript
interface ListString {
    Get__(index: int64): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true      | `List<string>`     | `ListString`    |
| true       | `ListString`    | `List<string>`     |

# parameterized_with_sub_type

```dexscript
interface SomeInf {
   <T>: interface{}
   get(): T
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true      | `SomeInf<interface{}>`     | `SomeInf<int64>`    |
| false       | `SomeInf<int64>`    | `SomeInf<interface{}>`     |