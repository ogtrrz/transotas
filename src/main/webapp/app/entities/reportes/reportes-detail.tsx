import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reportes.reducer';

export const ReportesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reportesEntity = useAppSelector(state => state.reportes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reportesDetailsHeading">
          <Translate contentKey="transotasApp.reportes.detail.title">Reportes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.id}</dd>
          <dt>
            <span id="titulo">
              <Translate contentKey="transotasApp.reportes.titulo">Titulo</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.titulo}</dd>
          <dt>
            <span id="caso">
              <Translate contentKey="transotasApp.reportes.caso">Caso</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.caso}</dd>
          <dt>
            <span id="img">
              <Translate contentKey="transotasApp.reportes.img">Img</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.img}</dd>
          <dt>
            <span id="autor">
              <Translate contentKey="transotasApp.reportes.autor">Autor</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.autor}</dd>
          <dt>
            <span id="tituloix">
              <Translate contentKey="transotasApp.reportes.tituloix">Tituloix</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.tituloix}</dd>
          <dt>
            <span id="autorix">
              <Translate contentKey="transotasApp.reportes.autorix">Autorix</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.autorix}</dd>
          <dt>
            <span id="fechaix">
              <Translate contentKey="transotasApp.reportes.fechaix">Fechaix</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.fechaix ? <TextFormat value={reportesEntity.fechaix} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="imgix">
              <Translate contentKey="transotasApp.reportes.imgix">Imgix</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.imgix}</dd>
          <dt>
            <span id="ciudad">
              <Translate contentKey="transotasApp.reportes.ciudad">Ciudad</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.ciudad}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="transotasApp.reportes.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.estado}</dd>
          <dt>
            <span id="pais">
              <Translate contentKey="transotasApp.reportes.pais">Pais</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.pais}</dd>
          <dt>
            <span id="extra1">
              <Translate contentKey="transotasApp.reportes.extra1">Extra 1</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra1}</dd>
          <dt>
            <span id="extra2">
              <Translate contentKey="transotasApp.reportes.extra2">Extra 2</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra2}</dd>
          <dt>
            <span id="extra3">
              <Translate contentKey="transotasApp.reportes.extra3">Extra 3</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra3}</dd>
          <dt>
            <span id="extra4">
              <Translate contentKey="transotasApp.reportes.extra4">Extra 4</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra4}</dd>
          <dt>
            <span id="extra5">
              <Translate contentKey="transotasApp.reportes.extra5">Extra 5</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra5}</dd>
          <dt>
            <span id="extra6">
              <Translate contentKey="transotasApp.reportes.extra6">Extra 6</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra6}</dd>
          <dt>
            <span id="extra7">
              <Translate contentKey="transotasApp.reportes.extra7">Extra 7</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra7}</dd>
          <dt>
            <span id="extra8">
              <Translate contentKey="transotasApp.reportes.extra8">Extra 8</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra8}</dd>
          <dt>
            <span id="extra9">
              <Translate contentKey="transotasApp.reportes.extra9">Extra 9</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra9}</dd>
          <dt>
            <span id="extra10">
              <Translate contentKey="transotasApp.reportes.extra10">Extra 10</Translate>
            </span>
          </dt>
          <dd>{reportesEntity.extra10}</dd>
          <dt>
            <Translate contentKey="transotasApp.reportes.informacion">Informacion</Translate>
          </dt>
          <dd>{reportesEntity.informacion ? reportesEntity.informacion.id : ''}</dd>
          <dt>
            <Translate contentKey="transotasApp.reportes.casoText">Caso Text</Translate>
          </dt>
          <dd>{reportesEntity.casoText ? reportesEntity.casoText.id : ''}</dd>
          <dt>
            <Translate contentKey="transotasApp.reportes.categorys">Categorys</Translate>
          </dt>
          <dd>
            {reportesEntity.categorys
              ? reportesEntity.categorys.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {reportesEntity.categorys && i === reportesEntity.categorys.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="transotasApp.reportes.comentarios">Comentarios</Translate>
          </dt>
          <dd>
            {reportesEntity.comentarios
              ? reportesEntity.comentarios.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {reportesEntity.comentarios && i === reportesEntity.comentarios.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/reportes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reportes/${reportesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReportesDetail;
