import { ProColumns, ProTable } from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React from 'react';

export type Props = {
  columns: ProColumns<API.ApiInfo>[];
  onCancel: () => void;
  onSubmit: (values: API.ApiInfo) => Promise<void>;
  visible: boolean;
};

const CreateModal: React.FC<Props> = (props) => {

  const { columns, onCancel, onSubmit, visible } = props;
  return (
    <Modal visible={visible} onCancel={onCancel}>
      <ProTable
        type={'form'}
        columns={columns}
        onSubmit={async (value) => {
          onSubmit?.(value);
        }}
      />
    </Modal>
  );
};
export default CreateModal;
