import { expect, test } from "@playwright/test";

test.beforeEach(async ({ page }) => {
  await page.route("**/api/v1/auth/refresh", (route) =>
    route.fulfill({ status: 401, json: { message: "No session" } })
  );
});

test("register page scrolls and Sign In returns to login", async ({ page }) => {
  await page.setViewportSize({ width: 1280, height: 560 });
  await page.goto("/register");

  await expect(page.getByRole("heading", { name: "Create Account" }))
    .toBeVisible();
  const scrollable = await page.evaluate(
    () => document.documentElement.scrollHeight > window.innerHeight
  );
  expect(scrollable).toBe(true);

  await page.getByRole("link", { name: "Sign In" }).click();
  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByRole("heading", { name: "Welcome Back" }))
    .toBeVisible();
});

test("root is canonicalized and unknown pages render a 404", async ({ page }) => {
  await page.goto("/");
  await expect(page).toHaveURL(/\/login$/);

  await page.goto("/does-not-exist");
  await expect(page.getByText("Page not found")).toBeVisible();
  await expect(page).toHaveURL(/does-not-exist$/);
});
