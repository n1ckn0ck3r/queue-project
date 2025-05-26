import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { 
  fetchGroups, 
  createGroup, 
  updateGroup, 
  deleteGroup,
  fetchDisciplines,
  createDiscipline,
  updateDiscipline,
  deleteDiscipline,
  resetAdminState
} from '../store/slices/adminSlice';
import { openModal, closeModal } from '../store/slices/uiSlice';
import '../styles/AdminPage.css';

const AdminPage = () => {
  const dispatch = useDispatch();
  const { groups, disciplines, isLoading, error } = useSelector((state) => state.admin);
  const { modal } = useSelector((state) => state.ui);
  const [activeTab, setActiveTab] = useState('groups');
  const [groupFormData, setGroupFormData] = useState({ groupName: '' });
  const [disciplineFormData, setDisciplineFormData] = useState({ disciplineName: '' });
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    dispatch(resetAdminState());
    dispatch(fetchGroups());
    dispatch(fetchDisciplines());
  }, [dispatch]);

  const handleTabChange = (tab) => {
    setActiveTab(tab);
  };

  // Group form handlers
  const handleGroupChange = (e) => {
    setGroupFormData({ ...groupFormData, [e.target.name]: e.target.value });
  };

  const handleGroupSubmit = (e) => {
    e.preventDefault();
    if (editingId) {
      dispatch(updateGroup({ id: editingId, updates: groupFormData }))
        .then(() => {
          setGroupFormData({ groupName: '' });
          setEditingId(null);
        });
    } else {
      dispatch(createGroup(groupFormData))
        .then(() => {
          setGroupFormData({ groupName: '' });
        });
    }
  };

  const handleEditGroup = (group) => {
    setGroupFormData({ groupName: group.groupName });
    setEditingId(group.id);
  };

  const handleDeleteGroup = (id) => {
    if (window.confirm('Вы уверены, что хотите удалить эту группу?')) {
      dispatch(deleteGroup(id));
    }
  };

  // Discipline form handlers
  const handleDisciplineChange = (e) => {
    setDisciplineFormData({ ...disciplineFormData, [e.target.name]: e.target.value });
  };

  const handleDisciplineSubmit = (e) => {
    e.preventDefault();
    if (editingId) {
      dispatch(updateDiscipline({ id: editingId, updates: disciplineFormData }))
        .then(() => {
          setDisciplineFormData({ disciplineName: '' });
          setEditingId(null);
        });
    } else {
      dispatch(createDiscipline(disciplineFormData))
        .then(() => {
          setDisciplineFormData({ disciplineName: '' });
        });
    }
  };

  const handleEditDiscipline = (discipline) => {
    setDisciplineFormData({ disciplineName: discipline.disciplineName });
    setEditingId(discipline.id);
  };

  const handleDeleteDiscipline = (id) => {
    if (window.confirm('Вы уверены, что хотите удалить эту дисциплину?')) {
      dispatch(deleteDiscipline(id));
    }
  };

  const handleCancelEdit = () => {
    if (activeTab === 'groups') {
      setGroupFormData({ groupName: '' });
    } else {
      setDisciplineFormData({ disciplineName: '' });
    }
    setEditingId(null);
  };

  if (isLoading && groups.length === 0 && disciplines.length === 0) {
    return (
      <div className="spinner-container">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="admin-page">
      <h1>Панель Администратора</h1>

      {error && (
        <div className="alert alert-error">
          {error.message || 'Произошла ошибка. Пожалуйста, попробуйте снова.'}
        </div>
      )}

      <div className="tabs">
        <button 
          className={`tab-button ${activeTab === 'groups' ? 'active' : ''}`} 
          onClick={() => handleTabChange('groups')}
        >
          Группы
        </button>
        <button 
          className={`tab-button ${activeTab === 'disciplines' ? 'active' : ''}`} 
          onClick={() => handleTabChange('disciplines')}
        >
          Дисциплины
        </button>
      </div>

      <div className="tab-content">
        {activeTab === 'groups' ? (
          <div className="groups-tab">
            <h2>Управление группами</h2>

            <form onSubmit={handleGroupSubmit} className="admin-form">
              <div className="form-group">
                <label htmlFor="groupName">Название группы</label>
                <input
                  type="text"
                  id="groupName"
                  name="groupName"
                  value={groupFormData.groupName}
                  onChange={handleGroupChange}
                  required
                />
              </div>
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingId ? 'Обновить группу' : 'Добавить группу'}
                </button>
                {editingId && (
                  <button type="button" className="btn btn-secondary" onClick={handleCancelEdit}>
                    Отмена
                  </button>
                )}
              </div>
            </form>

            <div className="item-list">
              <h3>Список групп</h3>
              {groups.length === 0 ? (
                <p>Группы не найдены.</p>
              ) : (
                <ul>
                  {groups.map((group) => (
                    <li key={group.id} className="item">
                      <span>{group.groupName}</span>
                      <div className="item-actions">
                        <button 
                          className="btn btn-sm btn-secondary"
                          onClick={() => handleEditGroup(group)}
                        >
                          Изменить
                        </button>
                        <button 
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDeleteGroup(group.id)}
                        >
                          Удалить
                        </button>
                      </div>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </div>
        ) : (
          <div className="disciplines-tab">
            <h2>Управление дисциплинами</h2>

            <form onSubmit={handleDisciplineSubmit} className="admin-form">
              <div className="form-group">
                <label htmlFor="disciplineName">Название дисциплины</label>
                <input
                  type="text"
                  id="disciplineName"
                  name="disciplineName"
                  value={disciplineFormData.disciplineName}
                  onChange={handleDisciplineChange}
                  required
                />
              </div>
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingId ? 'Обновить дисциплину' : 'Добавить дисциплину'}
                </button>
                {editingId && (
                  <button type="button" className="btn btn-secondary" onClick={handleCancelEdit}>
                    Отмена
                  </button>
                )}
              </div>
            </form>

            <div className="item-list">
              <h3>Список дисциплин</h3>
              {disciplines.length === 0 ? (
                <p>Дисциплины не найдены.</p>
              ) : (
                <ul>
                  {disciplines.map((discipline) => (
                    <li key={discipline.id} className="item">
                      <span>{discipline.disciplineName}</span>
                      <div className="item-actions">
                        <button 
                          className="btn btn-sm btn-secondary"
                          onClick={() => handleEditDiscipline(discipline)}
                        >
                          Изменить
                        </button>
                        <button 
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDeleteDiscipline(discipline.id)}
                        >
                          Удалить
                        </button>
                      </div>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminPage;
