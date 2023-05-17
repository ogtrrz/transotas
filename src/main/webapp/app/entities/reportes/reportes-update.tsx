import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInformacion } from 'app/shared/model/informacion.model';
import { getEntities as getInformacions } from 'app/entities/informacion/informacion.reducer';
import { ICaso } from 'app/shared/model/caso.model';
import { getEntities as getCasos } from 'app/entities/caso/caso.reducer';
import { ICategorys } from 'app/shared/model/categorys.model';
import { getEntities as getCategorys } from 'app/entities/categorys/categorys.reducer';
import { IComentarios } from 'app/shared/model/comentarios.model';
import { getEntities as getComentarios } from 'app/entities/comentarios/comentarios.reducer';
import { IReportes } from 'app/shared/model/reportes.model';
import { getEntity, updateEntity, createEntity, reset } from './reportes.reducer';

export const ReportesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const informacions = useAppSelector(state => state.informacion.entities);
  const casos = useAppSelector(state => state.caso.entities);
  const categorys = useAppSelector(state => state.categorys.entities);
  const comentarios = useAppSelector(state => state.comentarios.entities);
  const reportesEntity = useAppSelector(state => state.reportes.entity);
  const loading = useAppSelector(state => state.reportes.loading);
  const updating = useAppSelector(state => state.reportes.updating);
  const updateSuccess = useAppSelector(state => state.reportes.updateSuccess);

  const handleClose = () => {
    navigate('/reportes' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getInformacions({}));
    dispatch(getCasos({}));
    dispatch(getCategorys({}));
    dispatch(getComentarios({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fechaix = convertDateTimeToServer(values.fechaix);

    const entity = {
      ...reportesEntity,
      ...values,
      categorys: mapIdList(values.categorys),
      comentarios: mapIdList(values.comentarios),
      informacion: informacions.find(it => it.id.toString() === values.informacion.toString()),
      caso: casos.find(it => it.id.toString() === values.caso.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaix: displayDefaultDateTime(),
        }
      : {
          ...reportesEntity,
          fechaix: convertDateTimeFromServer(reportesEntity.fechaix),
          informacion: reportesEntity?.informacion?.id,
          caso: reportesEntity?.caso?.id,
          categorys: reportesEntity?.categorys?.map(e => e.id.toString()),
          comentarios: reportesEntity?.comentarios?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="transotasApp.reportes.home.createOrEditLabel" data-cy="ReportesCreateUpdateHeading">
            <Translate contentKey="transotasApp.reportes.home.createOrEditLabel">Create or edit a Reportes</Translate>
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
                  id="reportes-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('transotasApp.reportes.titulo')}
                id="reportes-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
              />
              <ValidatedField label={translate('transotasApp.reportes.caso')} id="reportes-caso" name="caso" data-cy="caso" type="text" />
              <ValidatedField label={translate('transotasApp.reportes.img')} id="reportes-img" name="img" data-cy="img" type="text" />
              <ValidatedField
                label={translate('transotasApp.reportes.autor')}
                id="reportes-autor"
                name="autor"
                data-cy="autor"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.tituloix')}
                id="reportes-tituloix"
                name="tituloix"
                data-cy="tituloix"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.autorix')}
                id="reportes-autorix"
                name="autorix"
                data-cy="autorix"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.fechaix')}
                id="reportes-fechaix"
                name="fechaix"
                data-cy="fechaix"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.imgix')}
                id="reportes-imgix"
                name="imgix"
                data-cy="imgix"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.ciudad')}
                id="reportes-ciudad"
                name="ciudad"
                data-cy="ciudad"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.estado')}
                id="reportes-estado"
                name="estado"
                data-cy="estado"
                type="text"
              />
              <ValidatedField label={translate('transotasApp.reportes.pais')} id="reportes-pais" name="pais" data-cy="pais" type="text" />
              <ValidatedField
                label={translate('transotasApp.reportes.extra1')}
                id="reportes-extra1"
                name="extra1"
                data-cy="extra1"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra2')}
                id="reportes-extra2"
                name="extra2"
                data-cy="extra2"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra3')}
                id="reportes-extra3"
                name="extra3"
                data-cy="extra3"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra4')}
                id="reportes-extra4"
                name="extra4"
                data-cy="extra4"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra5')}
                id="reportes-extra5"
                name="extra5"
                data-cy="extra5"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra6')}
                id="reportes-extra6"
                name="extra6"
                data-cy="extra6"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra7')}
                id="reportes-extra7"
                name="extra7"
                data-cy="extra7"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra8')}
                id="reportes-extra8"
                name="extra8"
                data-cy="extra8"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra9')}
                id="reportes-extra9"
                name="extra9"
                data-cy="extra9"
                type="text"
              />
              <ValidatedField
                label={translate('transotasApp.reportes.extra10')}
                id="reportes-extra10"
                name="extra10"
                data-cy="extra10"
                type="text"
              />
              <ValidatedField
                id="reportes-informacion"
                name="informacion"
                data-cy="informacion"
                label={translate('transotasApp.reportes.informacion')}
                type="select"
              >
                <option value="" key="0" />
                {informacions
                  ? informacions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="reportes-caso" name="caso" data-cy="caso" label={translate('transotasApp.reportes.caso')} type="select">
                <option value="" key="0" />
                {casos
                  ? casos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('transotasApp.reportes.categorys')}
                id="reportes-categorys"
                data-cy="categorys"
                type="select"
                multiple
                name="categorys"
              >
                <option value="" key="0" />
                {categorys
                  ? categorys.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('transotasApp.reportes.comentarios')}
                id="reportes-comentarios"
                data-cy="comentarios"
                type="select"
                multiple
                name="comentarios"
              >
                <option value="" key="0" />
                {comentarios
                  ? comentarios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reportes" replace color="info">
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

export default ReportesUpdate;
