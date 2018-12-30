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

