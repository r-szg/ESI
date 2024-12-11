import React from 'react';

interface TextAreaInputProps {
  label: string;
  name: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  rows?: number;
  required?: boolean;
}

const TextAreaInput: React.FC<TextAreaInputProps> = ({
  label,
  name,
  value,
  onChange,
  rows = 4,
  required = false,
}) => (
  <div className='p-1'>
    <label className="block text-sm font-medium text-gray-700">{label}</label>
    <textarea
      name={name}
      value={value}
      onChange={onChange}
      rows={rows}
      required={required}
      className="mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
    ></textarea>
  </div>
);

export default TextAreaInput;
