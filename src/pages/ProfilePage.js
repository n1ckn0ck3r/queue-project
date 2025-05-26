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
      if (user.user && user.user.id) {
        dispatch(fetchUserProfile(user.user.id));
        dispatch(fetchUserQueues(user.user.id));
      } else {
        dispatch(fetchUserProfile(user.id));
        dispatch(fetchUserQueues(user.id));
      }
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
        {error.message || 'Не удалось загрузить профиль. Пожалуйста, попробуйте снова.'}
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="alert alert-error">
        Профиль не найден.
      </div>
    );
  }

  return (
    <div>
      <h1 className="mb-4">Мой профиль</h1>

      <div className="queue-detail mb-4">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h3>Информация профиля</h3>
          <button className="btn btn-secondary" onClick={handleEditProfile}>
            Редактировать профиль
          </button>
        </div>

        <div className="mb-3">
          <p><strong>Имя пользователя:</strong> {profile.username}</p>
          <p><strong>Email:</strong> {profile.email}</p>
        </div>
      </div>

      <div className="queue-detail">
        <h3 className="mb-3">Мои очереди</h3>

        {userQueues.length === 0 ? (
          <div className="alert alert-info">
            Вы не состоите ни в одной очереди. <Link to="/queues">Просмотрите очереди</Link>, чтобы присоединиться.
          </div>
        ) : (
          <div className="queue-list">
            {userQueues.map((queue) => (
              <div key={queue.id} className="queue-card">
                <h3 className="queue-title">{queue.name}</h3>
                <p className="queue-info">
                  <strong>Дисциплина:</strong> {queue.discipline.disciplineName || 'Н/Д'}
                </p>
                <p className="queue-info">
                  <strong>Статус:</strong> {queue.active ? 'Активна' : 'Неактивна'}
                </p>
                <div className="queue-actions">
                  <Link to={`/queues/${queue.id}`} className="btn btn-primary">
                    Просмотр
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
              <h2>Редактировать профиль</h2>
              <button className="modal-close" onClick={handleCloseModal}>
                &times;
              </button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleSubmit}>
                <div className="form-group">
                  <label htmlFor="username" className="form-label">Имя пользователя</label>
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
                    {isLoading ? 'Сохранение...' : 'Сохранить изменения'}
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
