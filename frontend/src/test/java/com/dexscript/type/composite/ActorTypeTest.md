# no_type_param

```dexscript
function Hello(): string {
    return 'hello'
}
```

```dexscript
interface PromiseString {
    Consume__(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `string`   | `Hello()`      |
| true       | `PromiseString`   | `new Hello()`      |

# one_type_param

```dexscript
function Hello(<T>: interface{}): T {
    return ''
}
```

```dexscript
interface PromiseString {
    Consume__(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `string`   | `Hello<string>()`      |
| true       | `PromiseString`   | `new Hello<string>()`      |

