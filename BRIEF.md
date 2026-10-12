# Brief: wellbeing insights for employers

*From the founders, after a few calls with HR leads at client companies.*

## Why

Employers pay for mynd for their staff. The HR leads who buy it keep asking us the same thing:
"How is my team actually going?" Today they can't see anything, so when the contract comes up for
renewal they have no evidence that it's helping. We want to give them something useful, without
breaking the promise we make to every employee: **your individual data stays private, always.**

## What we want

An HR admin opens **Insights** in the app and sees how their organisation is feeling, broken down
by team, over the last few weeks.

Things the HR leads asked for, roughly in the order they asked:

1. See which teams are struggling the most this month.
2. See the trend week by week, so they can tell if things are getting better or worse.
3. See the most common moods in each team.
4. Filter by team, and by office location (we only have the user's timezone for that).
5. Click into a struggling team and see who might need a check-in from their manager.
6. Read the notes people write, so HR can understand why a team is struggling.
7. Export it to CSV for the monthly board pack.

## What we know

- Only HR admins (`role = hr_admin`) should see Insights, and only for their own company.
- Insights is a company-gated feature. The company has to have it switched on, and it must fail
  closed: if in doubt, the admin sees nothing.
- Some teams are very small. Northwind Freight's leadership team is 3 people.
- People check in irregularly. Some check in daily, some a couple of times a month.
- People are spread across timezones.

## What we haven't decided

Plenty. For example, we haven't defined what "struggling" means. Make the calls you need to make,
and write down what you decided and why in `NOTES.md`.
