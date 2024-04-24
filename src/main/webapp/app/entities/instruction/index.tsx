import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Instruction from './instruction';
import InstructionDetail from './instruction-detail';
import InstructionUpdate from './instruction-update';
import InstructionDeleteDialog from './instruction-delete-dialog';

const InstructionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Instruction />} />
    <Route path="new" element={<InstructionUpdate />} />
    <Route path=":id">
      <Route index element={<InstructionDetail />} />
      <Route path="edit" element={<InstructionUpdate />} />
      <Route path="delete" element={<InstructionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InstructionRoutes;
