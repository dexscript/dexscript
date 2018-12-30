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

