import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { register, resetAuthState } from '../store/slices/authSlice';

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [formErrors, setFormErrors] = useState({});
  
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isLoading, error, isAuthenticated } = useSelector((state) => state.auth);

  useEffect(() => {
    // Reset auth state when component mounts
    dispatch(resetAuthState());
  }, [dispatch]);

  useEffect(() => {
    // Redirect if authenticated
    if (isAuthenticated) {
      navigate('/queues');
    }
  }, [isAuthenticated, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const validateForm = () => {
    const errors = {};
    
    if (!formData.username) {
      errors.usename = 'Username is required';
    }
    
    if (!formData.email) {
      errors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = 'Email is invalid';
    }
    
    if (!formData.password) {
      errors.password = 'Password is required';
    } else if (formData.password.length < 6) {
      errors.password = 'Password must be at least 6 characters';
    }
    
    if (formData.password !== formData.confirmPassword) {
      errors.confirmPassword = 'Passwords do not match';
    }
    
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (validateForm()) {
      // Remove confirmPassword before sending to API
      const { confirmPassword, ...registrationData } = formData;
      dispatch(register(registrationData));
    }
  };

  return (
    <div className="form-container">
      <h2 className="form-title">Create an Account</h2>
      
      {error && (
        <div className="alert alert-error">
          {error.message || 'Failed to register. Please try again.'}
        </div>
      )}
      
      <form onSubmit={handleSubmit}>  
        <div className="form-group">
          <label htmlFor="username" className="form-label">Username</label>
          <input
            type="text"
            id="username"
            name="username"
            className="form-input"
            value={formData.username}
            onChange={handleChange}
          />
          {formErrors.username && <div className="form-error">{formErrors.username}</div>}
        </div>
        
        <div className="form-group">
          <label htmlFor="email" className="form-label">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            className="form-input"
            value={formData.email}
            onChange={handleChange}
          />
          {formErrors.email && <div className="form-error">{formErrors.email}</div>}
        </div>
        
        <div className="form-group">
          <label htmlFor="password" className="form-label">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            className="form-input"
            value={formData.password}
            onChange={handleChange}
          />
          {formErrors.password && <div className="form-error">{formErrors.password}</div>}
        </div>
        
        <div className="form-group">
          <label htmlFor="confirmPassword" className="form-label">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            className="form-input"
            value={formData.confirmPassword}
            onChange={handleChange}
          />
          {formErrors.confirmPassword && <div className="form-error">{formErrors.confirmPassword}</div>}
        </div>
        
        <button type="submit" className="form-button" disabled={isLoading}>
          {isLoading ? 'Registering...' : 'Register'}
        </button>
      </form>
      
      <div className="mt-3 text-center">
        <p>Already have an account? <Link to="/login">Login here</Link></p>
      </div>
    </div>
  );
};

export default RegisterPage;