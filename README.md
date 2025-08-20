# Command Line Interpreter Project

## Branching Strategy

To keep our work organized and clean, we are following this structure:

* **`main` branch**

  * This is our final, production-ready branch.
  * 🚫 **Do not push directly to `main`**.
  * You can only merge into `main` through a **Pull Request (PR)**.

* **`development` branch**

  * Used to integrate tasks before merging them into `main`.
  * You can push your finished task branch here after testing it.

* **Task branches**

  * Each task has its own branch following this naming convention:

    ```
    development/<task-name>
    ```
  * 🚫 Always switch to your **task branch** before working and pushing your changes.

---

##  Workflow with GitHub Actions

Inside the `.github/workflows/` folder, you’ll find our **YAML file**.

* This file configures **GitHub Actions**, which run automated checks (such as building and testing the project) whenever we push or open a Pull Request.
* Think of it as our project’s **automation script**: it makes sure everything works smoothly before merging changes.
Do you want me to also add a **visual diagram of the branching strategy** (like `main` ← `development` ← `task branches`) in the README? That usually helps teammates “get it” instantly.

