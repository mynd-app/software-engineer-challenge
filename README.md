# mynd Software Engineer challenge

Thanks for taking the time to do this. This repo is a small, made-up slice of a mynd-like
product: a Laravel API and a Kotlin Multiplatform Android app where employees check in with how
they feel. None of it is real mynd code or data.

## The rules

- **4 hours, hard stop.** Stop at 4 hours even if you're mid-task. Breaks don't count, and you
  can split the 4 hours across more than one sitting - just log your times in `NOTES.md`.
- **Setup doesn't count.** Getting Docker, Android Studio and the builds running is outside the
  4 hours. Do it first, then start the clock. If setup fights you for more than about 30 minutes,
  stop and tell us - that's useful to know, and we'll sort it out.
- **There is more here than fits in 4 hours.** That's deliberate. You won't finish everything,
  and you're not expected to. Choose what to work on.
- **Use your AI tools.** Claude Code, Cursor, Codex, whatever you normally use. We work this way
  every day. `AGENTS.md` holds the conventions for this repo (`CLAUDE.md` imports it).
- **We read your notes and commit history as closely as your code.**
- **Need an adjustment?** Tell us - more time, a different format, anything that helps.
- **Questions:** email danny@myndapp.com.au. We'll answer when we can, but don't wait for a
  reply. Write down what you assumed and keep going.

## Setup (not timed)

1. **Make your own private copy of this repo.** Please don't fork it, and don't push to this
   repo: other candidates have access to it, and a fork could be visible to them. Instead, clone
   it and push it to a new private repo on your own GitHub account:

   ```sh
   git clone https://github.com/mynd-app/software-engineer-challenge.git mynd-challenge
   cd mynd-challenge
   git remote remove origin
   gh repo create mynd-challenge --private --source . --push
   ```

   (Without the GitHub CLI: create an empty private repo on github.com, then
   `git remote add origin <its URL>` and `git push -u origin main`.)
2. **Share it with us.** In your new repo, go to Settings -> Collaborators and invite
   **`danny-mynd`**. Keep the repo private.
3. Backend: see [`backend/README.md`](backend/README.md). It runs in Docker on port 8080.
4. Android: see [`android/README.md`](android/README.md). Open the `android/` folder in Android
   Studio and run it on an emulator against your local backend.
5. Create a branch called `submission` and start your clock.

## The tasks

Do them in any order. Read all of them before you start.

1. **Fix the failing test.** One backend test fails. Work out why and fix it.
2. **Build the insights API.** Read [`BRIEF.md`](BRIEF.md) and build the backend for it.
3. **Build the insights screen.** Add it to the Android app, using your API.
4. **Review PR #42.** [`review/PR-42.md`](review/PR-42.md) is a pull request a teammate has asked
   you to review. Write your review in `review/REVIEW.md`, as you would leave it on GitHub.
5. **Plan what's next.** [`ROADMAP.md`](ROADMAP.md) has three pieces of upcoming work: a product
   request, a migration and an integration. Plan any you choose to pick up.
6. **Write your notes.** Fill in [`NOTES.md`](NOTES.md). Leave yourself time for this one.

## Handing in

- Commit as you go on your `submission` branch. Small commits with plain messages are great - we
  read the history.
- Push the branch to **your private repo** and open a pull request from `submission` into `main`
  there before your 4 hours are up. Leave `main` as it was. Put your start and end times in
  `NOTES.md`, then email danny@myndapp.com.au the link to your pull request.
- If you used an agent config file of your own (`CLAUDE.md`, `AGENTS.md`, rules files), commit it.
  If you're happy to, include a few of your key prompts or a short excerpt of an agent session in
  `NOTES.md`.

## What happens next

We'll book a 1-hour walk-through where you show us what you did, talk through your choices, and
tell us what you'd do next. For about 10 minutes of it, we'll ask you to extend your work live
with your own tools while we watch, so have your usual setup ready. It can be after hours if that
suits you better.
