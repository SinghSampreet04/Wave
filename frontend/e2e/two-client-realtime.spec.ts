import { expect, test } from "@playwright/test";

const userA = process.env.WAVE_E2E_USER_A;
const passwordA = process.env.WAVE_E2E_PASSWORD_A;
const userB = process.env.WAVE_E2E_USER_B;
const passwordB = process.env.WAVE_E2E_PASSWORD_B;

test("two clients synchronize a channel message", async ({ browser }) => {
  test.skip(
    !userA || !passwordA || !userB || !passwordB,
    "Set the WAVE_E2E_USER/PASSWORD_A/B deployment credentials."
  );

  const contextA = await browser.newContext();
  const contextB = await browser.newContext();
  const pageA = await contextA.newPage();
  const pageB = await contextB.newPage();

  const login = async (
    page: typeof pageA,
    email: string,
    password: string
  ) => {
    await page.goto("/login");
    await page.getByLabel("Email Address").fill(email);
    await page.getByLabel("Password").fill(password);
    await page.getByRole("button", { name: "Sign In" }).click();
    await expect(page).toHaveURL(/\/dashboard$/);
  };

  await login(pageA, userA!, passwordA!);
  await login(pageB, userB!, passwordB!);

  const text = `Realtime check ${Date.now()}`;
  await pageA.getByLabel("Message").fill(text);
  await pageA.getByRole("button", { name: "Send" }).click();
  await expect(pageB.getByText(text)).toBeVisible({ timeout: 10_000 });

  await contextA.close();
  await contextB.close();
});
