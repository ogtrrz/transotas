import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICasoText } from 'app/shared/model/caso-text.model';
import { getEntity, updateEntity, createEntity, reset } from './caso-text.reducer';

export const CasoTextUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const casoTextEntity = useAppSelector(state => state.casoText.entity);
  const loading = useAppSelector(state => state.casoText.loading);
  const updating = useAppSelector(state => state.casoText.updating);
  const updateSuccess = useAppSelector(state => state.casoText.updateSuccess);

  const handleClose = () => {
    navigate('/caso-text' + location.search);
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
      ...casoTextEntity,
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
          ...casoTextEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.casoText.home.createOrEditLabel" data-cy="CasoTextCreateUpdateHeading">
            <Translate contentKey="transotasApp.casoText.home.createOrEditLabel">Create or edit a CasoText</Translate>
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
                  id="caso-text-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('transotasApp.casoText.descripcion')}
                id="caso-text-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="textarea"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra1')}
                id="caso-text-extra1"
                name="extra1"
                data-cy="extra1"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra2')}
                id="caso-text-extra2"
                name="extra2"
                data-cy="extra2"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra3')}
                id="caso-text-extra3"
                name="extra3"
                data-cy="extra3"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra4')}
                id="caso-text-extra4"
                name="extra4"
                data-cy="extra4"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra5')}
                id="caso-text-extra5"
                name="extra5"
                data-cy="extra5"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra6')}
                id="caso-text-extra6"
                name="extra6"
                data-cy="extra6"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra7')}
                id="caso-text-extra7"
                name="extra7"
                data-cy="extra7"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra8')}
                id="caso-text-extra8"
                name="extra8"
                data-cy="extra8"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra9')}
                id="caso-text-extra9"
                name="extra9"
                data-cy="extra9"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.casoText.extra10')}
                id="caso-text-extra10"
                name="extra10"
                data-cy="extra10"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/caso-text" replace color="info">
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

export default CasoTextUpdate;
