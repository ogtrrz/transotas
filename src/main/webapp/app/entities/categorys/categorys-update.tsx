import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReportes } from 'app/shared/model/reportes.model';
import { getEntities as getReportes } from 'app/entities/reportes/reportes.reducer';
import { ICategorys } from 'app/shared/model/categorys.model';
import { getEntity, updateEntity, createEntity, reset } from './categorys.reducer';

export const CategorysUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reportes = useAppSelector(state => state.reportes.entities);
  const categorysEntity = useAppSelector(state => state.categorys.entity);
  const loading = useAppSelector(state => state.categorys.loading);
  const updating = useAppSelector(state => state.categorys.updating);
  const updateSuccess = useAppSelector(state => state.categorys.updateSuccess);

  const handleClose = () => {
    navigate('/categorys' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReportes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...categorysEntity,
      ...values,
      reportes: mapIdList(values.reportes),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...categorysEntity,
          reportes: categorysEntity?.reportes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.categorys.home.createOrEditLabel" data-cy="CategorysCreateUpdateHeading">
            <Translate contentKey="transotasApp.categorys.home.createOrEditLabel">Create or edit a Categorys</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="categorys-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('transotasApp.categorys.categoria')}
                id="categorys-categoria"
                name="categoria"
                data-cy="categoria"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra1')}
                id="categorys-extra1"
                name="extra1"
                data-cy="extra1"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra2')}
                id="categorys-extra2"
                name="extra2"
                data-cy="extra2"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra3')}
                id="categorys-extra3"
                name="extra3"
                data-cy="extra3"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra4')}
                id="categorys-extra4"
                name="extra4"
                data-cy="extra4"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra5')}
                id="categorys-extra5"
                name="extra5"
                data-cy="extra5"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra6')}
                id="categorys-extra6"
                name="extra6"
                data-cy="extra6"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra7')}
                id="categorys-extra7"
                name="extra7"
                data-cy="extra7"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra8')}
                id="categorys-extra8"
                name="extra8"
                data-cy="extra8"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra9')}
                id="categorys-extra9"
                name="extra9"
                data-cy="extra9"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.extra10')}
                id="categorys-extra10"
                name="extra10"
                data-cy="extra10"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.categorys.reportes')}
                id="categorys-reportes"
                data-cy="reportes"
                type="select"
                multiple
                name="reportes"
              >
                <option value="" key="0" />
                {reportes
                  ? reportes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/categorys" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CategorysUpdate;
