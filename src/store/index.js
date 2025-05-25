import { configureStore } from '@reduxjs/toolkit';
import authReducer from './slices/authSlice';
import queueReducer from './slices/queueSlice';
import profileReducer from './slices/profileSlice';
import uiReducer from './slices/uiSlice';
import adminReducer from './slices/adminSlice';

export const store = configureStore({
  reducer: {
    auth: authReducer,
    queue: queueReducer,
    profile: profileReducer,
    ui: uiReducer,
    admin: adminReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export default store;
