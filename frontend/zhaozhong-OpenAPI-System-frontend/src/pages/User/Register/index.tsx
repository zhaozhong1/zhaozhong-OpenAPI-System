import Footer from '@/components/Footer';
import {
  LockOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormText,
} from '@ant-design/pro-components';
import { useEmotionCss } from '@ant-design/use-emotion-css';
import { Helmet, history } from '@umijs/max';
import { Alert, message, Tabs } from 'antd';
import Settings from '../../../../config/defaultSettings';
import React, { useState } from 'react';
import {userRegisterUsingPost} from "@/services/zhaozhong-openApi-backend/userController";
import {SYSTEM_LOGO} from "@/constant/global_LOGO";
import {LOGIN_URI} from "@/constant/redirect_url";

const Register: React.FC = () => {
  const [type, setType] = useState<string>('account');

  const containerClassName = useEmotionCss(() => {
    return {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    };
  });




  const handleSubmit = async (values: API.UserRegisterRequest) => {



    const {userAccount,userPassword,checkPassword} = values;
    //前端校验
    if(userPassword !== checkPassword){
      message.error('两次输入的密码不一致。');
      return ;
    }

    try {
      // 注册
      const result = await userRegisterUsingPost({userAccount,userPassword,checkPassword});
      if (result.data) {
        const defaultRegisterSuccessMessage ='注册成功！';
        message.success(defaultRegisterSuccessMessage);

        const urlParams = new URL(window.location.href).searchParams;
        history.push(urlParams.get('redirect') || '/');
        return;
      }

      // 如果失败去设置用户错误信息
      throw new Error(`register error id = ${result.data}`);

    } catch (error) {
      const defaultRegisterFailureMessage = '注册失败，请重试！';
      console.log(error);
      message.error(defaultRegisterFailureMessage);
    }
  };

  return (
    <div className={containerClassName}>
      <Helmet>
        <title>
          {'注册'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          submitter={
            {
              searchConfig: {
                submitText: '注册',
              }
            }
          }
          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="兆中API开放平台"
          subTitle={
            <>
              <a href={LOGIN_URI} target={"_parent"} rel={"noreferrer"} style={{color:"blue",textDecoration:"none"}}>
                返回登录
              </a>
            </>
          }
          onFinish={async (values) => {
            await handleSubmit(values as API.UserRegisterRequest);
          }}
        >
          <Tabs
            activeKey={type}
            onChange={setType}
            centered
            items={[
              {
                key: 'account',
                label: '账户密码注册',
              },
            ]}
          />

          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined />,
                }}
                placeholder='输入注册账号'
                rules={[
                  {
                    required: true,
                    message: '请输入注册账号！'
                  },
                  {
                    required: false,
                    min: 4,
                    message: "账号长度不可小于4位！"
                  },
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder='输入注册密码'
                rules={[
                  {
                    required: true,
                    message: "请输入密码！",
                  },
                  {
                    required: false,
                    min: 8,
                    message: "密码长度不可小于8位！",
                  },
                ]}
              />
              <ProFormText.Password
                  name="checkPassword"
                  fieldProps={{
                      size: 'large',
                      prefix: <LockOutlined />,
                  }}
                  placeholder='确认密码'
                  rules={[
                      {
                          required: true,
                          message: "请确认密码！",
                      },
                      {
                          required: false,
                          min: 8,
                          message: "密码长度不可小于8位！",
                      }

                  ]}
              />
            </>
          )}
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Register;
