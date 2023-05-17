import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICaso } from 'app/shared/model/caso.model';
import { getEntity, updateEntity, createEntity, reset } from './caso.reducer';

export const CasoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const casoEntity = useAppSelector(state => state.caso.entity);
  const loading = useAppSelector(state => state.caso.loading);
  const updating = useAppSelector(state => state.caso.updating);
  const updateSuccess = useAppSelector(state => state.caso.updateSuccess);

  const handleClose = () => {
    navigate('/caso' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...casoEntity,
      ...values,
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
          ...casoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.caso.home.createOrEditLabel" data-cy="CasoCreateUpdateHeading">
            <Translate contentKey="transotasApp.caso.home.createOrEditLabel">Create or edit a Caso</Translate>
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
                  id="caso-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('transotasApp.caso.descripcion')}
                id="caso-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="textarea"
              />
              <ValidatedField label={translate('transotasApp.caso.extra1')} id="caso-extra1" name="extra1" data-cy="extra1" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra2')} id="caso-extra2" name="extra2" data-cy="extra2" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra3')} id="caso-extra3" name="extra3" data-cy="extra3" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra4')} id="caso-extra4" name="extra4" data-cy="extra4" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra5')} id="caso-extra5" name="extra5" data-cy="extra5" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra6')} id="caso-extra6" name="extra6" data-cy="extra6" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra7')} id="caso-extra7" name="extra7" data-cy="extra7" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra8')} id="caso-extra8" name="extra8" data-cy="extra8" type="text" />
              <ValidatedField label={translate('transotasApp.caso.extra9')} id="caso-extra9" name="extra9" data-cy="extra9" type="text" />
              <ValidatedField
                label={translate('transotasApp.caso.extra10')}
                id="caso-extra10"
                name="extra10"
                data-cy="extra10"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/caso" replace color="info">
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

export default CasoUpdate;
