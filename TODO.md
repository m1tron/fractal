TODO list:

- Switch to JSON format for import/export (Large Task)
  - Currently, Serialized Java Objects are used. But that leads to tight coupling with any projects that would rely on exports from this program.
- Animation (Large Task)
  - Motivation: It looks pretty satisfying when pulling the a square around, to see the result in realtime. Certain patterns, like moving a recursive pattern square in a circle is especially nice. Implementing this as a feature would be nice. But there needs to be some planning before.
- Remodel old buttons - DONE
  - Motivation: The old buttons used to update from the editor pane should be repurposed to fill the functionality of reset buttons. Currently, undoing a square dragged out of bounds is impossible
- Smart square adding
  - Motivation: Currently it can be frustrating when adding squares to more complex figures, since the program always puts it between the last and first squares in the array, which themselves are not visible to the user.
- Issue tracking exist here at the moment, but it would be nice to investigate a move to github tracked issues.



