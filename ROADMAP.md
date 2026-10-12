# Up next

Three pieces of work we'd like to start soon. For any you pick up, write a short plan in
`plans/<name>.md`: what you'd build, the decisions and risks, what you'd ask us, and how you'd
roll it out. Build some of it too, if you think that's the best use of your time.

---

## 1. Product request: shift-start check-ins for the Northwind warehouse

*From the founders*

Northwind Freight is rolling mynd out to its whole warehouse next month: about 300 people on
three shifts (6am, 2pm, 10pm), all at one site. Their operations manager wants check-ins to be
part of the shift briefing. Everyone opens the app at the start of the shift and checks in
before they head onto the floor.

- They'd like a push prompt at shift start, and a way for staff to set which shift they're on.
- Staff use the site wifi, and phones are not allowed on the floor, so the check-in has to work
  in the few minutes before the shift.
- We'd like to know whether we're ready for this, and what we'd need to change first.

---

## 2. Migration: store each check-in's local date

*From engineering*

We work out calendar dates from `created_at` and the user's *current* timezone. That gets
expensive for insights, and it's wrong when someone moves timezone: their history shifts by a
day. We want each check-in to store the local date and timezone at the moment it was made.

- Add `local_date` and `timezone` to `check_ins`, set on create.
- Backfill the existing rows. In production that's a few million rows in MySQL, and the app
  can't go down for it.
- Streaks, and the insights work, should use the stored local date.
- What would you check before and after running it in production?

---

## 3. Integration plan: sync teams and leavers from the client's HR system

*From the founders*

Clients keep team lists up to date in their HR system (Employment Hero, BambooHR, Workday).
Today an HR admin edits teams in mynd by hand, so they drift, and leavers keep their access. We
want a plan for syncing from the HR system.

- What should we sync, how often, and who owns the truth when the two disagree?
- What happens to a leaver: their access, their data, and their place in a team's insights?
- Teams change size when people move. How does that affect what insights can show?
- Some clients will want this switched on and some won't. Our support team also needs to look
  up a member's history when something goes wrong. How should access to all of this work?
- We don't need code for this one. A plan we could hand to whoever builds it is ideal.
