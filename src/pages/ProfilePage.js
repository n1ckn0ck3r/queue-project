import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { 
  fetchUserProfile, 
  fetchUserQueues, 
  updateUserProfile, 
  resetProfileState 
} from '../store/slices/profileSlice';
import { Link } from 'react-router-dom';
import { openModal, closeModal } from '../store/slices/uiSlice';

const ProfilePage = () => {
  const dispatch = useDispatch();
  const { profile, userQueues, isLoading, error } = useSelector((state) => state.profile);
  const { user } = useSelector((state) => state.auth);
  const { modal } = useSelector((state) => state.ui);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
  });
  
  useEffect(() => {
    if (user) {
      dispatch(resetProfileState());
      dispatch(fetchUserProfile(user.id));
      dispatch(fetchUserQueues(user.id));
    }
  }, [dispatch, user]);

  useEffect(() => {
    if (profile) {
      setFormData({
        username: profile.username || '',
        email: profile.email || '',
      });
    }
  }, [profile]);

  const handleEditProfile = () => {
    dispatch(openModal({ type: 'EDIT_PROFILE', data: profile }));
  };

  const handleCloseModal = () => {
    dispatch(closeModal());
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(updateUserProfile({ id: user.id, updates: formData }))
      .then(() => {
        dispatch(closeModal());
      });
  };

  if (isLoading && !profile) {
    return (
      <div className="spinner-container">
        <div className="spinner"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-error">
        {error.message || 'Failed to load profile. Please try again.'}
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="alert alert-error">
        Profile not found.
      </div>
    );
  }

  return (
    <div>
      <h1 className="mb-4">My Profile</h1>
      
      <div className="queue-detail mb-4">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h3>Profile Information</h3>
          <button className="btn btn-secondary" onClick={handleEditProfile}>
            Edit Profile
          </button>
        </div>
        
        <div className="mb-3">
          <p><strong>Username:</strong> {profile.username}</p>
          <p><strong>Email:</strong> {profile.email}</p>
        </div>
      </div>
      
      <div className="queue-detail">
        <h3 className="mb-3">My Queues</h3>
        
        {userQueues.length === 0 ? (
          <div className="alert alert-info">
            You are not in any queues. <Link to="/queues">Browse queues</Link> to join one.
          </div>
        ) : (
          <div className="queue-list">
            {userQueues.map((queue) => (
              <div key={queue.id} className="queue-card">
                <h3 className="queue-title">{queue.name}</h3>
                <p className="queue-info">
                  <strong>Discipline:</strong> {queue.discipline.disciplineName || 'N/A'}
                </p>
                <p className="queue-info">
                  <strong>Status:</strong> {queue.active ? 'Active' : 'Inactive'}
                </p>
                <div className="queue-actions">
                  <Link to={`/queues/${queue.id}`} className="btn btn-primary">
                    View
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
      
      {modal.isOpen && modal.type === 'EDIT_PROFILE' && (
        <div className="modal-backdrop">
          <div className="modal-content">
            <div className="modal-header">
              <h2>Edit Profile</h2>
              <button className="modal-close" onClick={handleCloseModal}>
                &times;
              </button>
            </div>
            <div className="modal-body">
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
                </div>
                
                <div className="form-group">
                  <button type="submit" className="form-button" disabled={isLoading}>
                    {isLoading ? 'Saving...' : 'Save Changes'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfilePage;