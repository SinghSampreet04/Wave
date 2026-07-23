import AppRouter from "./routes/AppRouter";
import AuthBootstrap from "./features/auth/components/AuthBootstrap";
import ErrorBoundary from "./components/feedback/ErrorBoundary";

function App() {
  return (
    <AuthBootstrap>
      <ErrorBoundary>
        <AppRouter />
      </ErrorBoundary>
    </AuthBootstrap>
  );
}

export default App;
