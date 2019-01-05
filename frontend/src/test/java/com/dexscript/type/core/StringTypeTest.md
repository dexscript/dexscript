# assignable_relationship

| assignable | to               | from             |
| ---------- | ---------------- | ---------------- |
| true       | `string`         | `string`         |
| true       | `string`         | `'hello'`        |
| true       | `string`         | `(const)'hello'` |
| false      | `'hello'`        | `string`         |
| true       | `'hello'`        | `'hello'`        |
| false      | `'hello'`        | `'world'`        |
| true       | `'hello'`        | `(const)'hello'` |
| false      | `'hello'`        | `(const)'world'` |
| false      | `(const)'hello'` | `string`         |
| false      | `(const)'hello'` | `'hello'`        |
| false      | `(const)'hello'` | `(const)'hello'` |

# equals_relationship

| equals | left             | right            |
| ------ | ---------------- | ---------------- |
| true   | `string`         | `string`         |
| false  | `string`         | `'hello'`        |
| false  | `string`         | `(const)'hello'` |
| true   | `'hello'`        | `'hello'`        |
| false  | `'hello'`        | `'world'`        |
| false  | `'hello'`        | `(const)'hello'` |
| true   | `(const)'hello'` | `(const)'hello'` |
| false  | `(const)'hello'` | `(const)'world'` |
