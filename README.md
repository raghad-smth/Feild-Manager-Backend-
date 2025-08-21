# Feild Manager Backend Repo Workflow  

## Branching Strategy
* **main** → stable, production-ready code. No direct pushes.
* **development** → base branch for active work. All features merge here first.
* **feature branches** → each task has its own branch, named:

  ```
  development-taskname
  ```
## Workflow for Each Task

1. Switch to development and pull latest updates:

   ```bash
   git checkout development
   git pull origin development
   ```
2. Create a new branch for your task:

   ```bash
   git checkout -b development-taskname
   ```
   Example: `development-registration`
3. Work on your task (add controllers, services, repos, models, etc.).
4. Commit and push your changes:

   ```bash
   git add .
   git commit -m "Describe your task here"
   git push -u origin development-taskname
   ```
5. Open a **Pull Request (PR)** into the `development` branch.
6. Once reviewed and approved, it will be merged into `development`.
7. Later, when the project is stable, `development` will be merged into `main`.

---

* This keeps **main clean**, ensures all work is reviewed, and avoids conflicts.
* Each teammate only sees others’ changes once they’re merged into `development`.
