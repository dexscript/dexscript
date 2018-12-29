# assignable_relationship

| assignable | to           | from           |
| ---------- | ------------ | -------------- |
| true       | `float64`    | `float64`      |
| false      | `float64`    | `float32`      |
| true       | `float64`    | `(const)100.0` |
| true       | `float64`    | `(const)100`   |
| true       | `float32`    | `float32`      |
| false      | `float32`    | `float64`      |
| true       | `float32`    | `(const)100.0` |
| true       | `float32`    | `(const)100`   |
| false      | `(const)100` | `float32`      |
| false      | `(const)100` | `float64`      |
| false      | `(const)100` | `(const)100`   |

# equals_relationship

| equals | to           | from         |
| ------ | ------------ | ------------ |
| true   | `float64`    | `float64`    |
| false  | `float64`    | `float32`    |
| true   | `float32`    | `float32`    |
| false  | `float64`    | `(const)100` |
| true   | `(const)100` | `(const)100` |