import authReducer, { resetAuthState } from '../../../store/slices/authSlice';

describe('authSlice', () => {
  const initialState = {
    user: null,
    isAuthenticated: false,
    isLoading: false,
    error: null,
  };

  it('should return the initial state', () => {
    expect(authReducer(undefined, { type: undefined })).toEqual({
      user: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,
    });
  });

  it('should handle resetAuthState', () => {
    const previousState = {
      user: { id: 1, name: 'Test User' },
      isAuthenticated: true,
      isLoading: true,
      error: { message: 'Some error' },
    };

    expect(authReducer(previousState, resetAuthState())).toEqual({
      user: { id: 1, name: 'Test User' },
      isAuthenticated: true,
      isLoading: false,
      error: null,
    });
  });

  // Test for login.pending action
  it('should handle login.pending', () => {
    const action = { type: 'auth/login/pending' };
    const state = authReducer(initialState, action);
    
    expect(state).toEqual({
      user: null,
      isAuthenticated: false,
      isLoading: true,
      error: null,
    });
  });

  // Test for login.fulfilled action
  it('should handle login.fulfilled', () => {
    const user = { id: 1, name: 'Test User', token: 'test-token' };
    const action = { 
      type: 'auth/login/fulfilled',
      payload: user
    };
    
    const state = authReducer(initialState, action);
    
    expect(state).toEqual({
      user,
      isAuthenticated: true,
      isLoading: false,
      error: null,
    });
  });

  // Test for login.rejected action
  it('should handle login.rejected', () => {
    const error = { message: 'Invalid credentials' };
    const action = { 
      type: 'auth/login/rejected',
      payload: error
    };
    
    const state = authReducer(initialState, action);
    
    expect(state).toEqual({
      user: null,
      isAuthenticated: false,
      isLoading: false,
      error,
    });
  });

  // Test for logout.fulfilled action
  it('should handle logout.fulfilled', () => {
    const previousState = {
      user: { id: 1, name: 'Test User' },
      isAuthenticated: true,
      isLoading: false,
      error: null,
    };
    
    const action = { type: 'auth/logout/fulfilled' };
    const state = authReducer(previousState, action);
    
    expect(state).toEqual({
      user: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,
    });
  });
});