import {
  Component,
  type ErrorInfo,
  type ReactNode,
} from "react";

import ErrorState from "./ErrorState";

interface ErrorBoundaryProps {
  children: ReactNode;

  fallback?: ReactNode;
}

interface ErrorBoundaryState {
  hasError: boolean;
}

export default class ErrorBoundary extends Component<
  ErrorBoundaryProps,
  ErrorBoundaryState
> {
  state: ErrorBoundaryState = {
    hasError: false,
  };

  static getDerivedStateFromError(): ErrorBoundaryState {
    return {
      hasError: true,
    };
  }

  componentDidCatch(
    error: Error,
    errorInfo: ErrorInfo
  ) {
    console.error(
      "ErrorBoundary caught an error:",
      error,
      errorInfo
    );
  }

  private handleRetry = () => {
    this.setState({
      hasError: false,
    });
  };

  render() {
    if (this.state.hasError) {
      return (
        this.props.fallback ?? (
          <ErrorState
            heading="Something went wrong"
            description="An unexpected error occurred while rendering this page."
            actionLabel="Try Again"
            onAction={this.handleRetry}
          />
        )
      );
    }

    return this.props.children;
  }
}