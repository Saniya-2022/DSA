# Remove Invalid Parentheses --- Approach 1: Minimum Removals First

## Idea

This approach first calculates the **minimum number of parentheses that
must be removed**.

Then it uses backtracking to generate all possible valid strings using
exactly that many removals.

For every character:

-   If it is `(` or `)`, we can **remove** it.
-   We can also **keep** it.
-   If we keep `(`, `op` increases.
-   If we keep `)`, `op` decreases.
-   If `op < 0`, the current branch is invalid and we stop it.
-   At the end, the string is accepted only when `r == 0` and `op == 0`.

------------------------------------------------------------------------

## Step 1: Calculate Minimum Removals

For example:

``` text
s = "()())()"
```

Scan the string:

``` text
(  → miss = 1
)  → miss = 0
(  → miss = 1
)  → miss = 0
)  → miss = -1 → one ')' must be removed
(  → miss = 1
)  → miss = 0
```

So:

``` text
removal = 1
```

The backtracking function must therefore use exactly one removal.

------------------------------------------------------------------------

## Step 2: Backtracking

At a parenthesis, there are two choices:

``` text
                 current parenthesis
                  /                \
             REMOVE               KEEP
                |                   |
          don't add it          add it to ans
                                update op
```

The code uses a `StringBuilder` for `ans`.

When a character is kept:

``` java
ans.append(ch);
valid(...);
ans.deleteCharAt(ans.length() - 1);
```

The last line is the **undo/backtracking step**.

Example:

``` text
ans = "("

KEEP ')'
   ↓
ans = "()"

explore the complete subtree
   ↓
return

delete last character
   ↓
ans = "("
```

This restores `ans` so another branch can be explored.

------------------------------------------------------------------------

## Java Code

``` java
import java.util.*;

class Solution {
     public static HashSet<String> set = new HashSet<>();

    public static void valid(int r, String s, StringBuilder ans,
                             int i, int op) {

        if (op < 0)
            return;

        if (i == s.length()) {

            if (r == 0 && op == 0) {
                set.add(ans.toString());
            }

            return;
        }

        char ch = s.charAt(i);

        if (r > 0 && (s.charAt(i) == '(' || s.charAt(i) == ')'))
            valid(r - 1, s, ans, i + 1, op);

        ans.append(ch);

        if (ch == '(')
            valid(r, s, ans, i + 1, op + 1);

        else if (ch == ')')
            valid(r, s, ans, i + 1, op - 1);

        else
            valid(r, s, ans, i + 1, op);

        ans.deleteCharAt(ans.length() - 1);
    }

    public List<String> removeInvalidParentheses(String s) {

        set.clear();

        int miss = 0;
        int removal = 0;

        for (char ch : s.toCharArray()) {

            if (ch == '(')
                miss++;

            else if (ch == ')')
                miss--;

            if (miss < 0) {
                removal += Math.abs(miss);
                miss = 0;
            }
        }

        if (miss != 0)
            removal += miss;

        int r = removal;

        valid(r, s, new StringBuilder(), 0, 0);

        List<String> res = new ArrayList<>(set);

        return res;
    }
}
```

> **Important:** I corrected the condition from the pasted version:
>
> ``` java
> if(r>0 && s.charAt(i)=='('||s.charAt(i)==')')
> ```
>
> to:
>
> ``` java
> if(r > 0 && (s.charAt(i) == '(' || s.charAt(i) == ')'))
> ```
>
> Without the parentheses, Java interprets `&&` before `||`, so a `)`
> could enter the removal branch even when `r == 0`.

------------------------------------------------------------------------

## Example

Input:

``` text
()())()
```

Minimum removals:

``` text
1
```

The algorithm can produce:

``` text
(())()
()()()
```

Both are balanced and both remove exactly one parenthesis.

Therefore both are stored in the `HashSet`.

------------------------------------------------------------------------

## Why `deleteCharAt()` Is Necessary

Suppose:

``` text
ans = "("
```

We choose to keep `)`:

``` java
ans.append(')');
```

Now:

``` text
ans = "()"
```

The recursive call explores **everything below this KEEP decision**.

When it returns, `ans` is still:

``` text
"()"
```

But we need to go back to the previous state:

``` text
"("
```

Therefore:

``` java
ans.deleteCharAt(ans.length() - 1);
```

does:

``` text
"()" → "("
```

So the backtracking pattern is:

``` text
APPEND
   ↓
EXPLORE
   ↓
RETURN
   ↓
DELETE / UNDO
```

------------------------------------------------------------------------

## Complexity

There can be up to `2^n` KEEP/REMOVE choices in the worst case.

Therefore the backtracking search can take:

``` text
O(2^n)
```

in the worst case, apart from the cost of constructing/storing strings.

The `HashSet` removes duplicate results.

------------------------------------------------------------------------

## Summary

``` text
1. Calculate minimum removals.
2. Start backtracking.
3. For each parenthesis:
      - REMOVE it
      - KEEP it
4. If op < 0, stop the branch.
5. At the end:
      - r must be 0
      - op must be 0
6. Store the valid string.
7. Use append → recurse → delete to backtrack.
```
