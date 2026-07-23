import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import AuthLayout from "./AuthLayout";

describe("AuthLayout", () => {
  it("keeps long authentication forms vertically scrollable", () => {
    const { container } = render(
      <AuthLayout>
        <div>Authentication form</div>
      </AuthLayout>
    );

    const layout = container.firstElementChild;
    expect(layout).toHaveClass("min-h-screen", "overflow-x-hidden");
    expect(layout).not.toHaveClass("overflow-hidden");
    expect(screen.getByText("Authentication form")).toBeVisible();
  });
});
