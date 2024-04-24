import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './instruction.reducer';

export const InstructionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const instructionEntity = useAppSelector(state => state.instruction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="instructionDetailsHeading">Instruction</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{instructionEntity.id}</dd>
          <dt>
            <span id="stepNumber">Step Number</span>
          </dt>
          <dd>{instructionEntity.stepNumber}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{instructionEntity.description}</dd>
          <dt>Recipe</dt>
          <dd>{instructionEntity.recipe ? instructionEntity.recipe.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/instruction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/instruction/${instructionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InstructionDetail;
